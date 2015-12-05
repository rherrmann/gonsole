package com.codeaffine.gonsole.internal.command;

import static com.codeaffine.gonsole.internal.command.ShowInGitConsoleHandler.COMMAND_ID;
import static com.codeaffine.gonsole.test.io.Files.toCanonicalFile;
import static com.codeaffine.gonsole.test.util.workbench.PartHelper.INTRO_VIEW_ID;
import static java.util.Collections.EMPTY_MAP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.ui.ISources.ACTIVE_CURRENT_SELECTION_NAME;
import static org.eclipse.ui.ISources.ACTIVE_PART_NAME;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.ide.IDE;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.codeaffine.console.core.internal.GenericConsole;
import com.codeaffine.gonsole.internal.GitConsoleConfigurer;
import com.codeaffine.gonsole.test.resources.ProjectHelper;
import com.codeaffine.gonsole.test.util.workbench.ConsoleHelper;
import com.codeaffine.gonsole.test.util.workbench.PartHelper;

public class ShowInGitConsoleHandlerPDETest {

  @Rule
  public final TemporaryFolder tempFolder = new TemporaryFolder();

  private IWorkbench workbench;
  private ConsoleHelper consoleHelper;
  private PartHelper partHelper;
  private ProjectHelper projectHelper;
  private Repository repository;

  @Before
  public void setUp() throws IOException, GitAPIException {
    workbench = PlatformUI.getWorkbench();
    consoleHelper = new ConsoleHelper();
    partHelper = new PartHelper();
    partHelper.hideView( INTRO_VIEW_ID );
    repository = Git.init().setDirectory( tempFolder.newFolder() ).call().getRepository();
  }

  @After
  public void tearDown() throws CoreException {
    if( projectHelper != null ) {
      projectHelper.dispose();
    }
    consoleHelper.hideConsoleView();
    partHelper.closeEditors();
  }

  @Test
  public void testExecute() {
    projectHelper = new ProjectHelper( repository.getWorkTree() );
    ISelection selection = new StructuredSelection( projectHelper.getProject() );

    executeShowInGitConsoleHandler( selection );

    assertThatConsoleViewIsActive();
    assertThat( getCurrentConsoleRepository() ).isEqualTo( toCanonicalFile( repository.getDirectory() ) );
  }

  @Test
  public void testExecuteTwiceWithSameLocation() {
    projectHelper = new ProjectHelper( repository.getWorkTree() );
    ISelection selection = new StructuredSelection( projectHelper.getProject() );
    executeShowInGitConsoleHandler( selection );

    executeShowInGitConsoleHandler( selection );

    assertThatConsoleViewIsActive();
    assertThat( consoleHelper.getConsoles() ).hasSize( 1 );
    assertThat( getCurrentConsoleRepository() ).isEqualTo( toCanonicalFile( repository.getDirectory() ) );
  }

  @Test
  public void testExecuteTwiceWithoutLocation() {
    executeShowInGitConsoleHandler( StructuredSelection.EMPTY );

    executeShowInGitConsoleHandler( StructuredSelection.EMPTY );

    assertThatConsoleViewIsActive();
    assertThat( consoleHelper.getConsoles() ).hasSize( 1 );
    assertThat( getCurrentConsoleRepository() ).isNull();
  }

  @Test
  public void testExecuteWithEmptySelection() {
    executeShowInGitConsoleHandler( StructuredSelection.EMPTY );

    assertThatConsoleViewIsActive();
    assertThat( getCurrentConsoleRepository() ).isNull();
  }

  @Test
  public void testExecuteWithUnsharedResource() throws IOException {
    projectHelper = new ProjectHelper( tempFolder.newFolder() );
    ISelection selection = new StructuredSelection( projectHelper.getProject() );

    executeShowInGitConsoleHandler( selection );

    assertThatConsoleViewIsActive();
    assertThat( getCurrentConsoleRepository() ).isNull();
  }

  @Test
  public void testExecuteWithUnsharedResourceInEditor() throws IOException, CoreException {
    projectHelper = new ProjectHelper( tempFolder.newFolder() );
    openResourceInEditor();

    executeShowInGitConsoleHandler( new TextSelection( 0, 0 ) );

    assertThatConsoleViewIsActive();
    assertThat( getCurrentConsoleRepository() ).isNull();
  }

  @Test
  public void testExecuteWithSharedResourceInEditor() throws CoreException {
    projectHelper = new ProjectHelper( repository.getWorkTree() );
    openResourceInEditor();

    executeShowInGitConsoleHandler( new TextSelection( 0, 0 ) );

    assertThatConsoleViewIsActive();
    assertThat( getCurrentConsoleRepository() ).isEqualTo( toCanonicalFile( repository.getDirectory() ) );
  }

  private void assertThatConsoleViewIsActive() {
    IConsoleView consoleView = consoleHelper.getConsoleView();
    assertThat( consoleView.getSite().getPage().getActivePart() ).isEqualTo( consoleView );
  }

  private void openResourceInEditor() throws CoreException, PartInitException {
    IFile file = projectHelper.createFile( "file.txt", "content" );
    IDE.openEditor( partHelper.getActivePage(), file );
  }

  private void executeShowInGitConsoleHandler( ISelection selection ) {
    Command command = getShowInGitConsoleCommand();
    EvaluationContext context = new EvaluationContext( null, new Object() );
    context.addVariable( ACTIVE_PART_NAME, partHelper.getActivePage().getActivePart() );
    context.addVariable( ACTIVE_CURRENT_SELECTION_NAME, selection );
    ExecutionEvent event = new ExecutionEvent( command, EMPTY_MAP, null, context );
    new ShowInGitConsoleHandler().execute( event );
  }

  private Command getShowInGitConsoleCommand() {
    ICommandService commandService = ICommandService.class.cast( workbench.getService( ICommandService.class ) );
    return commandService.getCommand( COMMAND_ID );
  }

  private File getCurrentConsoleRepository() {
    GenericConsole console = ( GenericConsole )consoleHelper.getConsoles()[ 0 ];
    GitConsoleConfigurer consoleConfigurer = ( GitConsoleConfigurer )console.getConsoleConfigurer();
    consoleConfigurer.getRepositoryProvider().getCurrentRepositoryLocation();
    return consoleConfigurer.getRepositoryProvider().getCurrentRepositoryLocation();
  }

}
