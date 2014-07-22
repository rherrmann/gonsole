package com.codeaffine.gonsole.egit.internal.actions;

import java.io.File;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.codeaffine.console.core.ConsoleViewOpener;
import com.codeaffine.gonsole.GitConsoleFactory;

public class OpenConsoleActionDelegate implements IObjectActionDelegate {

  private IWorkbenchPart activePart;
  private File selectedRepositoryLocation;

  @Override
  public void setActivePart( IAction action, IWorkbenchPart targetPart ) {
    activePart = targetPart;
    updateActionEnablement( action );
  }

  @Override
  public void selectionChanged( IAction action, ISelection selection ) {
    SelectedRepositoryComputer computer = new SelectedRepositoryComputer( selection, activePart );
    selectedRepositoryLocation = computer.compute();
    updateActionEnablement( action );
  }

  @Override
  public void run( IAction action ) {
    openConsoleView();
    openConsole();
  }

  private void updateActionEnablement( IAction action ) {
    action.setEnabled( activePart != null && selectedRepositoryLocation != null );
  }

  private void openConsoleView() {
    new ConsoleViewOpener( activePart.getSite().getPage() ).open();
  }

  private void openConsole() {
    new GitConsoleFactory( selectedRepositoryLocation ).openConsole();
  }
}
