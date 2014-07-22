package com.codeaffine.gonsole.internal.command;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.codeaffine.console.core.ConsoleViewOpener;
import com.codeaffine.gonsole.GitConsoleFactory;


public class OpenGitConsoleHandler extends AbstractHandler {

  public static final String COMMAND_ID = "com.codeaffine.gonsole.internal.command.OpenGitConsole";

  @Override
  public Object execute( ExecutionEvent event ) {
    openConsoleView( event );
    openConsole( event );
    return null;
  }

  private static void openConsoleView( ExecutionEvent event ) {
    IWorkbenchPart activePart = HandlerUtil.getActivePart( event );
    new ConsoleViewOpener( activePart.getSite().getPage() ).open();
  }

  private static void openConsole( ExecutionEvent event ) {
    File initialRepositoryLocation = getInitialRepositoryLocation( event );
    new GitConsoleFactory( initialRepositoryLocation ).openConsole();
  }

  private static File getInitialRepositoryLocation( ExecutionEvent event ) {
    ISelection selection = HandlerUtil.getCurrentSelection( event );
    IWorkbenchPart activePart = HandlerUtil.getActivePart( event );
    return new RepositoryLocationComputer( selection, activePart ).compute();
  }
}
