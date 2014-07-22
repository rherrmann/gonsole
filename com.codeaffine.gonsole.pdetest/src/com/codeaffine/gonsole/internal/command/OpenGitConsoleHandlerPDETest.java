package com.codeaffine.gonsole.internal.command;

import static com.codeaffine.gonsole.internal.command.OpenGitConsoleHandler.COMMAND_ID;
import static com.codeaffine.test.util.workbench.PartHelper.CONSOLE_VIEW_ID;
import static com.codeaffine.test.util.workbench.PartHelper.INTRO_VIEW_ID;
import static java.util.Collections.EMPTY_MAP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.ui.ISources.ACTIVE_CURRENT_SELECTION_NAME;
import static org.eclipse.ui.ISources.ACTIVE_PART_NAME;

import java.io.File;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.codeaffine.console.core.internal.GenericConsole;
import com.codeaffine.gonsole.internal.GitConsoleConfigurer;
import com.codeaffine.test.resources.ProjectHelper;
import com.codeaffine.test.util.swt.DisplayHelper;
import com.codeaffine.test.util.workbench.PartHelper;

public class OpenGitConsoleHandlerPDETest {

  @Rule
  public final TemporaryFolder tempFolder = new TemporaryFolder();

  private IWorkbench workbench;
  private PartHelper partHelper;
  private ProjectHelper projectHelper;
  private Repository repository;

  @Test
  public void testExecute() {
    projectHelper = new ProjectHelper( repository.getWorkTree() );
    ISelection selection = new StructuredSelection( projectHelper.getProject() );

    executeOpenGitConsoleHandler( selection );

    IConsoleView consoleView = getConsoleView();
    assertThat( consoleView ).isNotNull();
    assertThat( consoleView.getSite().getPage().getActivePart() ).isEqualTo( consoleView );
    assertThat( getCurrentConsoleRepository() ).isEqualTo( repository.getDirectory() );
  }

  @Test
  public void testExecuteWithEmptySelection() {
    executeOpenGitConsoleHandler( StructuredSelection.EMPTY );

    IConsoleView consoleView = getConsoleView();
    assertThat( consoleView ).isNotNull();
    assertThat( consoleView.getSite().getPage().getActivePart() ).isEqualTo( consoleView );
    assertThat( getCurrentConsoleRepository() ).isNull();
  }

  @Before
  public void setUp() throws GitAPIException {
    workbench = PlatformUI.getWorkbench();
    partHelper = new PartHelper();
    partHelper.hideView( INTRO_VIEW_ID );
    repository = Git.init().setDirectory( tempFolder.getRoot() ).call().getRepository();
  }

  @After
  public void tearDown() throws CoreException {
    if( projectHelper != null ) {
      projectHelper.dispose();
    }
    partHelper.hideView( CONSOLE_VIEW_ID );
    removeAllConsoles();
  }

  private void executeOpenGitConsoleHandler( ISelection selection ) {
    Command command = getOpenGitConsoleCommand();
    EvaluationContext context = new EvaluationContext( null, new Object() );
    context.addVariable( ACTIVE_PART_NAME, workbench.getActiveWorkbenchWindow().getActivePage().getActivePart() );
    context.addVariable( ACTIVE_CURRENT_SELECTION_NAME, selection );
    ExecutionEvent event = new ExecutionEvent( command, EMPTY_MAP, null, context );
    new OpenGitConsoleHandler().execute( event );
  }

  private Command getOpenGitConsoleCommand() {
    ICommandService commandService = ( ICommandService )workbench.getService( ICommandService.class );
    return commandService.getCommand( COMMAND_ID );
  }

  private static File getCurrentConsoleRepository() {
    GenericConsole console = ( GenericConsole )ConsolePlugin.getDefault().getConsoleManager().getConsoles()[ 0 ];
    GitConsoleConfigurer consoleConfigurer = ( GitConsoleConfigurer )console.getConsoleConfigurer();
    consoleConfigurer.getRepositoryProvider().getCurrentRepositoryLocation();
    return consoleConfigurer.getRepositoryProvider().getCurrentRepositoryLocation();
  }

  private IConsoleView getConsoleView() {
    long start = System.currentTimeMillis();
    IViewPart result = partHelper.findView( CONSOLE_VIEW_ID );
    while( result == null && System.currentTimeMillis() - start < 5000 ) {
      new DisplayHelper().flushPendingEvents();
      result = partHelper.findView( CONSOLE_VIEW_ID );
    }
    while( result != workbench.getActiveWorkbenchWindow().getActivePage().getActivePart() ) {
      new DisplayHelper().flushPendingEvents();
    }
    return ( IConsoleView )result;
  }

  private static void removeAllConsoles() {
    IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    consoleManager.removeConsoles( consoleManager.getConsoles() );
  }
}
