package com.codeaffine.console.core.pdetest.bot;

import static com.codeaffine.test.util.workbench.PartHelper.CONSOLE_VIEW_ID;
import static com.codeaffine.test.util.workbench.PartHelper.INTRO_VIEW_ID;
import static com.google.common.base.Preconditions.checkState;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
import com.codeaffine.test.util.swt.DisplayHelper;
import com.codeaffine.test.util.swt.SWTEventHelper;
import com.codeaffine.test.util.workbench.PartHelper;

public class ConsoleBot implements MethodRule {

  private final PartHelper partHelper;
  final DisplayHelper displayHelper;
  GenericConsole console;
  ConsolePageBot consolePageBot;
  ConsoleConfigurer consoleConfigurer;

  public ConsoleBot() {
    partHelper = new PartHelper();
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

  public void typeKey( int modifiers, int keyCode ) {
    consolePageBot.triggerEvent( SWT.KeyDown, modifiers, keyCode );
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

  public void waitForCommandLineProcessing() {
    consolePageBot.waitForCommandLineProcessing();
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
    partHelper.hideView( INTRO_VIEW_ID );
    console = registerNewGitConsole( consoleConfigurer );
    showInView( console );
    consolePageBot = new ConsolePageBot( console.getPage() );
    consolePageBot.waitForInitialPrompt();
    return console;
  }

  public void reopenConsoleView() {
    partHelper.hideView( CONSOLE_VIEW_ID );
    partHelper.showView( CONSOLE_VIEW_ID );
    displayHelper.flushPendingEvents();
    consolePageBot = new ConsolePageBot( console.getPage() );
    consolePageBot.waitForInitialPrompt();
  }

  void cleanup() {
    removeGitConsoles();
    cleaHistory();
    consolePageBot = null;
    consoleConfigurer = null;
    partHelper.hideView( CONSOLE_VIEW_ID );
    displayHelper.dispose();
  }

  Table getContentProposalTable() {
    Table result = null;
    for( Shell popupShell : displayHelper.getNewShells() ) {
      Control firstChild = popupShell.getChildren()[ 0 ];
      if( firstChild instanceof Table ) {
        result = ( Table )firstChild;
      }
    }
    return result;
  }

  String getAdditionalInfoText() {
    String result = null;
    for( Shell popupShell : displayHelper.getNewShells() ) {
      if( isAdditionalInfoPopup( popupShell ) ) {
        Composite composite = ( Composite )popupShell.getChildren()[ 0 ];
        result = ( ( StyledText )composite.getChildren()[ 0 ] ).getText();
      }
    }
    return result;
  }

  private static boolean isAdditionalInfoPopup( Shell popupShell ) {
    return !( popupShell.getChildren()[ 0 ] instanceof Table );
  }

  private static GenericConsole registerNewGitConsole( ConsoleConfigurer consoleConfigurer ) {
    GenericConsole result = new GenericConsole( consoleConfigurer );
    ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[] { result } );
    return result;
  }

  private void showInView( IConsole console ) {
    IConsoleView consoleView = ( IConsoleView )partHelper.showView( CONSOLE_VIEW_ID );
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

  private void cleaHistory() {
    if( console != null ) {
      console.clearHistory();
    }
  }
}