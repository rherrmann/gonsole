package com.codeaffine.gonsole.egit.internal.actions;

import java.io.File;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

import com.codeaffine.gonsole.GitConsoleFactory;

public class OpenConsoleActionDelegate implements IObjectActionDelegate {

  public static final String CONSOLE_VIEW_ID = "org.eclipse.ui.console.ConsoleView";

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
    IViewPart visibleConsoleView = getVisibleConsoleView();
    if( visibleConsoleView == null ) {
      showConsoleView();
    } else {
      activateConsoleView();
    }
  }

  private void activateConsoleView() {
    activePart.getSite().getPage().activate( getVisibleConsoleView() );
  }

  private IViewPart getVisibleConsoleView() {
    IViewPart result = null;
    IWorkbenchPage page = activePart.getSite().getPage();
    for( IViewReference viewReference : page.getViewReferences() ) {
      if( CONSOLE_VIEW_ID.equals( viewReference.getId() ) ) {
        IViewPart view = viewReference.getView( false );
        if( view != null && page.isPartVisible( view ) ) {
          result = view;
        }
      }
    }
    return result;
  }

  private void showConsoleView() {
    try {
      activePart.getSite().getPage().showView( CONSOLE_VIEW_ID );
    } catch( PartInitException pie ) {
      throw new RuntimeException( pie );
    }
  }

  private void openConsole() {
    GitConsoleFactory consoleFactory = new GitConsoleFactory( selectedRepositoryLocation );
    consoleFactory.openConsole();
  }
}
