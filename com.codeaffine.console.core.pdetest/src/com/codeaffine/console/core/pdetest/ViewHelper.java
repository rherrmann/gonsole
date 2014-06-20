package com.codeaffine.console.core.pdetest;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.codeaffine.test.util.swt.DisplayHelper;

public class ViewHelper {

  private final IWorkbenchPage activePage;
  private final DisplayHelper displayHelper;

  public ViewHelper() {
    activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    displayHelper = new DisplayHelper();
  }

  public void hideView( String viewId ) {
    IViewPart view = activePage.findView( viewId );
    if( view != null ) {
      activePage.hideView( view );
      displayHelper.flushPendingEvents();
    }
  }

  public IViewPart showView( String viewId ) {
    try {
      return activePage.showView( viewId );
    } catch( PartInitException pie ) {
      throw new RuntimeException( pie );
    }
  }
}