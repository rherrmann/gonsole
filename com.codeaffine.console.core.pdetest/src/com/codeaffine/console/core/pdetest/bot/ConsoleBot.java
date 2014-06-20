package com.codeaffine.console.core.pdetest.bot;

import static com.google.common.base.Preconditions.checkState;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.codeaffine.console.core.ConsoleDefinition;
import com.codeaffine.console.core.internal.Console;
import com.codeaffine.console.core.pdetest.ViewHelper;
import com.codeaffine.test.util.swt.DisplayHelper;
import com.codeaffine.test.util.swt.SWTEventHelper;

public class ConsoleBot implements MethodRule {

  private static final String CONSOLE_VIEW_ID = "org.eclipse.ui.console.ConsoleView";
  private static final String INTRO_VIEW_ID = "org.eclipse.ui.internal.introview";

  private final ViewHelper viewHelper;
  final DisplayHelper displayHelper;

  ConsolePageBot gitConsolePageBot;
  ConsoleDefinition consoleDefinition;

  public ConsoleBot() {
    viewHelper = new ViewHelper();
    displayHelper = new DisplayHelper();
  }

  @Override
  public Statement apply( Statement base, FrameworkMethod method, Object target ) {
    return new ConsoleBotStatement( this, base );
  }

  public ConsoleBot typeText( String text ) {
    for( int i = 0; i < text.length(); i++ ) {
      gitConsolePageBot.triggerEvent( SWT.KeyDown, SWT.NONE, text.charAt( i ) );
      gitConsolePageBot.triggerEvent( SWT.KeyUp, SWT.NONE, text.charAt( i ) );
    }
    return this;
  }

  public void typeKey( int modifiers, char character ) {
    gitConsolePageBot.triggerEvent( SWT.KeyDown, modifiers, character );
  }

  public void typeEnter() {
    enterCommandLine( "" );
    gitConsolePageBot.waitForChange();
  }

  public void selectText( int start, int length ) {
    gitConsolePageBot.selectText( start, length );
  }

  public ConsoleBot positionCaret( int caretOffset ) {
    gitConsolePageBot.setCaretOffset( caretOffset );
    return this;
  }

  public void enterCommandLine( String commandLine ) {
    checkState( gitConsolePageBot != null, "GitConsole has not been opened yet." );

    String lineDelimiter = gitConsolePageBot.getLineDelimiter();
    gitConsolePageBot.append( commandLine + lineDelimiter );
  }

  public void runToolBarAction( String text ) {
    gitConsolePageBot.runToolBarAction( text );
  }

  public void selectFirstContentProposal() {
    Table table = getContentProposalTable();
    SWTEventHelper
      .trigger( SWT.DefaultSelection )
      .withIndex( 0 )
      .withItem( table.getItem( 0 ) )
      .on( table );
  }

  public Image getContentProposalImage( int index ) {
    Table table = getContentProposalTable();
    return table.getItem( index ).getImage();
  }

  public Console open( ConsoleDefinition consoleDefinition ) {
    this.consoleDefinition = consoleDefinition;
    viewHelper.hideView( INTRO_VIEW_ID );
    Console result = registerNewGitConsole( consoleDefinition );
    showInView( result );
    gitConsolePageBot = new ConsolePageBot( result.getPage() );
    gitConsolePageBot.waitForChange();
    return result;
  }

  void cleanup() {
    removeGitConsoles();
    gitConsolePageBot = null;
    consoleDefinition = null;
    viewHelper.hideView( CONSOLE_VIEW_ID );
    displayHelper.dispose();
  }

  private static Console registerNewGitConsole( ConsoleDefinition consoleDefinition ) {
    Console result = new Console( consoleDefinition );
    ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[] { result } );
    return result;
  }

  private IConsoleView showInView( IConsole console ) {
    IConsoleView result = ( IConsoleView )viewHelper.showView( CONSOLE_VIEW_ID );
    result.display( console );
    new DisplayHelper().flushPendingEvents();
    return result;
  }

  private static void removeGitConsoles() {
    IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    for( IConsole console : consoleManager.getConsoles() ) {
      if( console instanceof Console ) {
        consoleManager.removeConsoles( new IConsole[] { console } );
      }
    }
  }

  private Table getContentProposalTable() {
    Shell shell = displayHelper.getNewShells()[ 0 ];
    return ( Table )shell.getChildren()[ 0 ];
  }
}