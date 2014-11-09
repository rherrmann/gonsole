package com.codeaffine.gonsole.egit.internal.actions;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.gonsole.test.util.workbench.PartHelper.CONSOLE_VIEW_ID;
import static com.codeaffine.gonsole.test.util.workbench.PartHelper.INTRO_VIEW_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.egit.pdetest.EGitRepositoryHelper;
import com.codeaffine.gonsole.test.resources.ProjectHelper;
import com.codeaffine.gonsole.test.util.workbench.PartHelper;


public class OpenConsoleActionDelegatePDETest {

  @Rule
  public final EGitRepositoryHelper repositoryHelper = new EGitRepositoryHelper();

  private PartHelper partHelper;
  private ProjectHelper projectHelper;
  private IAction action;
  private OpenConsoleActionDelegate actionDelegate;

  @Test
  public void testRun() {
    File repositoryLocation = repositoryHelper.createRegisteredRepository( "repo" );
    projectHelper = new ProjectHelper( repositoryLocation.getParentFile() );

    runActionDelegate( projectHelper.getProject() );

    IViewPart consoleView = getConsoleView();
    assertThat( consoleView ).isNotNull();
    assertThat( consoleView.getSite().getPage().getActivePart() ).isEqualTo( consoleView );
  }

  @Test
  public void testRunWhileConsoleViewIsVisible() {
    partHelper.showView( CONSOLE_VIEW_ID );
    File repositoryLocation = repositoryHelper.createRegisteredRepository( "repo" );
    projectHelper = new ProjectHelper( repositoryLocation.getParentFile() );

    runActionDelegate( projectHelper.getProject() );

    IViewPart consoleView = getConsoleView();
    assertThat( consoleView ).isNotNull();
    assertThat( consoleView.getSite().getPage().getActivePart() ).isEqualTo( consoleView );
  }

  @Before
  public void setUp() {
    partHelper = new PartHelper();
    partHelper.hideView( INTRO_VIEW_ID );
    action = mock( IAction.class );
    actionDelegate = new OpenConsoleActionDelegate();
  }

  @After
  public void tearDown() throws CoreException {
    if( projectHelper != null ) {
      projectHelper.dispose();
    }
    partHelper.hideView( CONSOLE_VIEW_ID );
    removeAllConsoles();
  }

  private IViewPart getConsoleView() {
    long start = System.currentTimeMillis();
    IViewPart result = partHelper.findView( CONSOLE_VIEW_ID );
    while( result == null && System.currentTimeMillis() - start < 10000 ) {
      flushPendingEvents();
      result = partHelper.findView( CONSOLE_VIEW_ID );
    }
    return result;
  }

  private void runActionDelegate( Object selectedElement ) {
    actionDelegate.setActivePart( action, getActivePart() );
    actionDelegate.selectionChanged( action, new StructuredSelection( selectedElement ) );
    actionDelegate.run( action );
  }

  private static IWorkbenchPart getActivePart() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
  }

  private static void removeAllConsoles() {
    IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    consoleManager.removeConsoles( consoleManager.getConsoles() );
  }
}
