package com.codeaffine.console.core;

import java.util.stream.Stream;

import org.eclipse.ui.IViewPart;
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
    return Stream.of( page.getViewReferences() )
      .filter( viewReference -> CONSOLE_VIEW_ID.equals( viewReference.getId() ) )
      .map( viewReference -> viewReference.getView( false ) )
      .filter( view -> view != null && page.isPartVisible( view ) )
      .findFirst()
      .orElse( null );
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
