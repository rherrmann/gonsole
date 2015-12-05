package com.codeaffine.gonsole.test.util.workbench;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class PartHelper {

  public static final String INTRO_VIEW_ID = "org.eclipse.ui.internal.introview";
  public static final String CONSOLE_VIEW_ID = "org.eclipse.ui.console.ConsoleView";
  public static final String PROBLEM_VIEW_ID = "org.eclipse.ui.views.ProblemView";

  private final IWorkbenchPage activePage;

  public PartHelper() {
    activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
  }

  public IWorkbenchPage getActivePage() {
    return activePage;
  }

  public IViewPart findView( String viewId ) {
    return activePage.findView( viewId );
  }

  public void hideView( String viewId ) {
    IViewPart view = findView( viewId );
    if( view != null ) {
      activePage.hideView( view );
      flushPendingEvents();
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