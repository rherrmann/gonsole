package com.codeaffine.console.core;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.IConsoleConstants;


public class ConsoleViewOpener {
  private static final String CONSOLE_VIEW_ID = IConsoleConstants.ID_CONSOLE_VIEW;

  private final IWorkbenchPage page;

  public ConsoleViewOpener( IWorkbenchPage page ) {
    this.page = page;
  }

  public void open() {
    IViewPart visibleConsoleView = getVisibleConsoleView();
    if( visibleConsoleView == null ) {
      showConsoleView();
    } else {
      activateConsoleView();
    }
  }

  private IViewPart getVisibleConsoleView() {
    IViewPart result = null;
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

  private void activateConsoleView() {
    page.activate( getVisibleConsoleView() );
  }

  private void showConsoleView() {
    try {
      page.showView( CONSOLE_VIEW_ID );
    } catch( PartInitException pie ) {
      throw new RuntimeException( pie );
    }
  }
}
