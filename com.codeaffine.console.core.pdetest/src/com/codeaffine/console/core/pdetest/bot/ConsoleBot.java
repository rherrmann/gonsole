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

import com.codeaffine.console.core.ConsoleConfigurer;
import com.codeaffine.console.core.internal.GenericConsole;
import com.codeaffine.console.core.pdetest.ViewHelper;
import com.codeaffine.test.util.swt.DisplayHelper;
import com.codeaffine.test.util.swt.SWTEventHelper;

public class ConsoleBot implements MethodRule {

  public static final String CONSOLE_VIEW_ID = "org.eclipse.ui.console.ConsoleView";
  private static final String INTRO_VIEW_ID = "org.eclipse.ui.internal.introview";

  private final ViewHelper viewHelper;
  final DisplayHelper displayHelper;
  GenericConsole console;
  ConsolePageBot consolePageBot;
  ConsoleConfigurer consoleConfigurer;

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
      consolePageBot.triggerEvent( SWT.KeyDown, SWT.NONE, text.charAt( i ) );
      consolePageBot.triggerEvent( SWT.KeyUp, SWT.NONE, text.charAt( i ) );
    }
    return this;
  }

  public void typeKey( int modifiers, char character ) {
    consolePageBot.triggerEvent( SWT.KeyDown, modifiers, character );
  }

  public void typeEnter() {
    enterCommandLine( "" );
    consolePageBot.waitForCommandLineProcessing();
  }

  public void selectText( int start, int length ) {
    consolePageBot.selectText( start, length );
  }

  public ConsoleBot positionCaret( int caretOffset ) {
    consolePageBot.setCaretOffset( caretOffset );
    return this;
  }

  public void enterCommandLine( String commandLine ) {
    checkState( consolePageBot != null, "GitConsole has not been opened yet." );

    consolePageBot.enterCommandLine( commandLine );
  }

  public void runToolBarAction( String text ) {
    consolePageBot.runToolBarAction( text );
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

  public GenericConsole open( ConsoleConfigurer consoleConfigurer ) {
    this.consoleConfigurer = consoleConfigurer;
    viewHelper.hideView( INTRO_VIEW_ID );
    console = registerNewGitConsole( consoleConfigurer );
    showInView( console );
    consolePageBot = new ConsolePageBot( console.getPage() );
    consolePageBot.waitForInitialPrompt();
    return console;
  }

  public void reopenConsoleView() {
    viewHelper.hideView( CONSOLE_VIEW_ID );
    viewHelper.showView( CONSOLE_VIEW_ID );
    displayHelper.flushPendingEvents();
    consolePageBot = new ConsolePageBot( console.getPage() );
    consolePageBot.waitForInitialPrompt();
  }

  void cleanup() {
    removeGitConsoles();
    consolePageBot = null;
    consoleConfigurer = null;
    viewHelper.hideView( CONSOLE_VIEW_ID );
    displayHelper.dispose();
  }

  private static GenericConsole registerNewGitConsole( ConsoleConfigurer consoleConfigurer ) {
    GenericConsole result = new GenericConsole( consoleConfigurer );
    ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[] { result } );
    return result;
  }

  private void showInView( IConsole console ) {
    IConsoleView consoleView = ( IConsoleView )viewHelper.showView( CONSOLE_VIEW_ID );
    consoleView.display( console );
    new DisplayHelper().flushPendingEvents();
  }

  private static void removeGitConsoles() {
    IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    for( IConsole console : consoleManager.getConsoles() ) {
      if( console instanceof GenericConsole ) {
        consoleManager.removeConsoles( new IConsole[] { console } );
      }
    }
  }

  private Table getContentProposalTable() {
    Shell shell = displayHelper.getNewShells()[ 0 ];
    return ( Table )shell.getChildren()[ 0 ];
  }
}