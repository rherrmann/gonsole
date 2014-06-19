package com.codeaffine.gonsole.pdetest;

import static com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper.createWithSingleChildProvider;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;

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

import com.codeaffine.gonsole.internal.GitConsole;
import com.codeaffine.test.util.swt.DisplayHelper;
import com.codeaffine.test.util.swt.SWTEventHelper;

public class GitConsoleBot implements MethodRule {

  private static final String CONSOLE_VIEW_ID = "org.eclipse.ui.console.ConsoleView";
  private static final String INTRO_VIEW_ID = "org.eclipse.ui.internal.introview";

  private final ViewHelper viewHelper;
  final DisplayHelper displayHelper;
  GitConsolePageBot gitConsolePageBot;

  public GitConsoleBot() {
    viewHelper = new ViewHelper();
    displayHelper = new DisplayHelper();
  }

  @Override
  public Statement apply( Statement base, FrameworkMethod method, Object target ) {
    return new GitConsoleBotStatement( this, base );
  }

  public GitConsoleBot typeText( String text ) {
    for( int i = 0; i < text.length(); i++ ) {
      gitConsolePageBot.triggerEvent( SWT.KeyDown, SWT.NONE, text.charAt( i ) );
      gitConsolePageBot.triggerEvent( SWT.KeyUp, SWT.NONE, text.charAt( i ) );
    }
    return this;
  }

  public GitConsoleBot typeKey( int modifiers, char character ) {
    gitConsolePageBot.triggerEvent( SWT.KeyDown, modifiers, character );
    return this;
  }

  public GitConsoleBot typeEnter() {
    enterCommandLine( "" );
    gitConsolePageBot.waitForChange();
    return this;
  }

  public GitConsoleBot selectText( int start, int length ) {
    gitConsolePageBot.selectText( start, length );
    return this;
  }

  public GitConsoleBot positionCaret( int caretOffset ) {
    gitConsolePageBot.setCaretOffset( caretOffset );
    return this;
  }

  public GitConsoleBot enterCommandLine( String commandLine ) {
    checkState( gitConsolePageBot != null, "GitConsole has not been opened yet." );

    String lineDelimiter = gitConsolePageBot.getLineDelimiter();
    gitConsolePageBot.append( commandLine + lineDelimiter );
    return this;
  }

  public GitConsoleBot runToolBarAction( String text ) {
    gitConsolePageBot.runToolBarAction( text );
    return this;
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

  public GitConsole open( File ... repositoryLocations ) {
    viewHelper.hideView( INTRO_VIEW_ID );
    GitConsole result = registerNewGitConsole( repositoryLocations );
    showInView( result );
    gitConsolePageBot = new GitConsolePageBot( result.getPage() );
    gitConsolePageBot.waitForChange();
    return result;
  }

  void cleanup() {
    removeGitConsoles();
    gitConsolePageBot = null;
    viewHelper.hideView( CONSOLE_VIEW_ID );
    displayHelper.dispose();
  }

  private static GitConsole registerNewGitConsole( File[] repositoryLocations ) {
    GitConsole result = new GitConsole( createWithSingleChildProvider( repositoryLocations ) );
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
      if( console instanceof GitConsole ) {
        consoleManager.removeConsoles( new IConsole[] { console } );
      }
    }
  }

  private Table getContentProposalTable() {
    Shell shell = displayHelper.getNewShells()[ 0 ];
    return ( Table )shell.getChildren()[ 0 ];
  }
}