package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper.createWithSingleChildProvider;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.codeaffine.gonsole.internal.GitConsole;
import com.codeaffine.gonsole.pdetest.ViewHelper;
import com.codeaffine.test.util.swt.DisplayHelper;

public class GitConsoleBot implements MethodRule {

  private static final String CONSOLE_VIEW_ID = "org.eclipse.ui.console.ConsoleView";
  private static final String INTRO_VIEW_ID = "org.eclipse.ui.internal.introview";

  private final ViewHelper viewHelper;
  GitConsolePageBot gitConsolePageBot;

  public GitConsoleBot() {
    viewHelper = new ViewHelper();
  }

  @Override
  public Statement apply( Statement base, FrameworkMethod method, Object target ) {
    return new GitConsoleBotStatement( this, base );
  }

  public GitConsoleBot typeText( String text ) {
    for( int i = 0; i < text.length(); i++ ) {
      gitConsolePageBot.triggerEvent( SWT.KeyDown, text.charAt( i ) );
      gitConsolePageBot.triggerEvent( SWT.KeyUp, text.charAt( i ) );
    }
    return this;
  }

  public GitConsoleBot positionCaret( int caretOffset ) {
    gitConsolePageBot.setCaretOffset( caretOffset );
    return this;
  }

  public void enterCommandLine( String commandLine ) {
    checkState( gitConsolePageBot != null, "GitConsole has not been opened yet." );

    String lineDelimiter = gitConsolePageBot.getLineDelimiter();
    gitConsolePageBot.append( commandLine + lineDelimiter );
  }

  public void open( File ... repositoryLocations ) {
    viewHelper.hideView( INTRO_VIEW_ID );
    GitConsole console = registerNewGitConsole( repositoryLocations );
    showInView( console );
    gitConsolePageBot = new GitConsolePageBot( console.getPage() );
    gitConsolePageBot.waitForChange();
  }

  void cleanup() {
    removeGitConsoles();
    gitConsolePageBot = null;
    viewHelper.hideView( CONSOLE_VIEW_ID );
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
}