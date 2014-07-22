package com.codeaffine.console.core;

import static com.codeaffine.test.util.workbench.PartHelper.CONSOLE_VIEW_ID;
import static com.codeaffine.test.util.workbench.PartHelper.INTRO_VIEW_ID;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.test.util.swt.DisplayHelper;
import com.codeaffine.test.util.workbench.PartHelper;

public class ConsoleViewOpenerPDETest {

  private PartHelper partHelper;
  private IWorkbenchPage activePage;

  @Test
  public void testOpen() {
    new ConsoleViewOpener( activePage ).open();

    assertThatConsoleViewIsActive();
  }

  @Test
  public void testOpenTwice() {
    new ConsoleViewOpener( activePage ).open();
    int viewCount = activePage.getViewReferences().length;

    new ConsoleViewOpener( activePage ).open();

    assertThatConsoleViewIsActive();
    assertThat( activePage.getViewReferences().length ).isEqualTo( viewCount );
  }

  @Test
  public void testOpenWithInactiveVisibleConsoleView() {
    partHelper.showView( CONSOLE_VIEW_ID );
    partHelper.showView( PartHelper.PROBLEM_VIEW_ID );

    new ConsoleViewOpener( activePage ).open();

    assertThatConsoleViewIsActive();
  }

  @Before
  public void setUp() {
    activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    partHelper = new PartHelper();
    partHelper.hideView( INTRO_VIEW_ID );
  }

  @After
  public void tearDown() {
    partHelper.hideView( CONSOLE_VIEW_ID );
  }

  private IViewPart getConsoleView() {
    long start = System.currentTimeMillis();
    IViewPart result = partHelper.findView( CONSOLE_VIEW_ID );
    while( result == null && System.currentTimeMillis() - start < 5000 ) {
      new DisplayHelper().flushPendingEvents();
      result = partHelper.findView( CONSOLE_VIEW_ID );
    }
    return result;
  }

  private void assertThatConsoleViewIsActive() {
    IViewPart consoleView = getConsoleView();
    assertThat( consoleView ).isNotNull();
    assertThat( consoleView.getSite().getPage().getActivePart() ).isEqualTo( consoleView );
  }
}
