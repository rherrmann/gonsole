package com.codeaffine.test.util.workbench;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.codeaffine.test.util.swt.DisplayHelper;

public class PartHelper {

  public static final String INTRO_VIEW_ID = "org.eclipse.ui.internal.introview";
  public static final String CONSOLE_VIEW_ID = "org.eclipse.ui.console.ConsoleView";

  private final IWorkbenchPage activePage;
  private final DisplayHelper displayHelper;

  public PartHelper() {
    activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    displayHelper = new DisplayHelper();
  }

  public IViewPart findView( String viewId ) {
    return activePage.findView( viewId );
  }

  public void hideView( String viewId ) {
    IViewPart view = findView( viewId );
    if( view != null ) {
      activePage.hideView( view );
      displayHelper.flushPendingEvents();
    }
  }

  public ViewPart showView( String viewId ) {
    try {
      return ( ViewPart )activePage.showView( viewId );
    } catch( PartInitException pie ) {
      throw new RuntimeException( pie );
    }
  }

  public void closeEditors() {
    activePage.closeAllEditors( false );
  }
}