package com.codeaffine.gonsole.egit.internal.actions;

import static com.codeaffine.test.util.workbench.PartHelper.CONSOLE_VIEW_ID;
import static com.codeaffine.test.util.workbench.PartHelper.INTRO_VIEW_ID;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.egit.pdetest.EGitRepositoryHelper;
import com.codeaffine.test.resources.ProjectHelper;
import com.codeaffine.test.resources.ResourceHelper;
import com.codeaffine.test.util.workbench.PartHelper;

public class SelectedRepositoryComputerPDETest {

  @Rule
  public final EGitRepositoryHelper repositoryHelper = new EGitRepositoryHelper();

  private PartHelper partHelper;
  private ProjectHelper projectHelper;

  @Test
  public void testComputeWithSharedResource() {
    File repositoryLocation = createSharedProject();

    File selectedRepositoryLocation = computeSelectedRepository( projectHelper.getProject() );

    assertThat( selectedRepositoryLocation ).isEqualTo( repositoryLocation );
  }

  @Test
  public void testComputeWithSharedFileInEditor() throws CoreException {
    File repositoryLocation = createSharedProject();
    IFile file = projectHelper.createFile( "file.txt", "" );
    IEditorPart editor = openEditor( file );

    File selectedRepositoryLocation = computeSelectedRepository( editor );

    assertThat( selectedRepositoryLocation ).isEqualTo( repositoryLocation );
  }

  @Test
  public void testComputeWithUnsharedResource() {
    projectHelper = new ProjectHelper();

    File selectedRepositoryLocation = computeSelectedRepository( projectHelper.getProject() );

    assertThat( selectedRepositoryLocation ).isNull();
  }

  @Test
  public void testComputeWithUnsharedFileInEditor() throws CoreException {
    projectHelper = new ProjectHelper();
    IFile file = projectHelper.createFile( "file.txt", "" );
    IEditorPart editor = openEditor( file );

    File selectedRepositoryLocation = computeSelectedRepository( editor );

    assertThat( selectedRepositoryLocation ).isNull();
  }

  @Test
  public void testComputeWithNonExistingResource() throws CoreException {
    File repositoryLocation = createSharedProject();
    IFile file = projectHelper.createFile( "file.txt", "" );
    ResourceHelper.delete( file );

    File selectedRepositoryLocation = computeSelectedRepository( file );

    assertThat( selectedRepositoryLocation ).isEqualTo( repositoryLocation );
  }

  @Before
  public void setUp() {
    partHelper = new PartHelper();
    partHelper.hideView( INTRO_VIEW_ID );
  }

  @After
  public void tearDown() throws CoreException {
    if( projectHelper != null ) {
      projectHelper.dispose();
    }
    partHelper.hideView( CONSOLE_VIEW_ID );
    partHelper.closeEditors();
  }

  private static File computeSelectedRepository( IResource resource ) {
    return computeSelectedRepository( new StructuredSelection( resource ), null );
  }

  private static File computeSelectedRepository( IEditorPart editor ) {
    return computeSelectedRepository( new TextSelection( 0, 0 ), editor );
  }

  private static File computeSelectedRepository( ISelection selection, IEditorPart editor ) {
    SelectedRepositoryComputer computer = new SelectedRepositoryComputer( selection, editor );
    return computer.compute();
  }

  private static IEditorPart openEditor( IFile file ) throws PartInitException {
    return IDE.openEditor( getActivePage(), file );
  }

  private static IWorkbenchPage getActivePage() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
  }

  private File createSharedProject() {
    File result = repositoryHelper.createRegisteredRepository( "repo" );
    projectHelper = new ProjectHelper( result.getParentFile() );
    return result;
  }

}
