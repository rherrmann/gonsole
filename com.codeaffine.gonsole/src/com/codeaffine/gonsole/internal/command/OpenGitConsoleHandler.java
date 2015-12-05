package com.codeaffine.gonsole.internal.command;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;

import com.codeaffine.console.core.ConsoleViewOpener;
import com.codeaffine.gonsole.GitConsoleFactory;


public class OpenGitConsoleHandler extends AbstractHandler {

  public static final String COMMAND_ID = "com.codeaffine.gonsole.internal.command.OpenGitConsole";

  @Override
  public Object execute( ExecutionEvent event ) {
    WorkbenchState workbenchState = new WorkbenchState( event );
    openConsoleView( workbenchState );
    openConsole( workbenchState );
    return null;
  }

  private static void openConsoleView( WorkbenchState workbenchState ) {
    new ConsoleViewOpener( workbenchState.getActivePart().getSite().getPage() ).open();
  }

  private static void openConsole( WorkbenchState workbenchState ) {
    File initialRepositoryLocation = new RepositoryLocationComputer( workbenchState ).compute();
    new GitConsoleFactory( initialRepositoryLocation ).openConsole();
  }
}
