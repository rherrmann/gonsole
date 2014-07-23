package com.codeaffine.gonsole.internal.command;

import static com.codeaffine.test.util.workbench.PartHelper.INTRO_VIEW_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jface.viewers.StructuredSelection.EMPTY;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.codeaffine.test.resources.ProjectHelper;
import com.codeaffine.test.util.workbench.PartHelper;

public class RepositoryLocationComputerPDETest {

  @Rule
  public final TemporaryFolder tempFolder = new TemporaryFolder();

  private PartHelper partHelper;
  private ProjectHelper projectHelper;
  private Repository repository;

  @Test
  public void testComputeWithEmptySelection() {
    File repositoryLocation = new RepositoryLocationComputer( EMPTY, null ).compute();

    assertThat( repositoryLocation ).isNull();
  }

  @Test
  public void testComputeWithUnsharedProject() {
    projectHelper = new ProjectHelper( tempFolder.getRoot() );

    File repositoryLocation = computeRepositoryLocation( projectHelper.getProject() );

    assertThat( repositoryLocation ).isNull();
  }

  @Test
  public void testComputeWithSharedProject() {
    projectHelper = new ProjectHelper( repository.getWorkTree() );

    File repositoryLocation = computeRepositoryLocation( projectHelper.getProject() );

    assertThat( repositoryLocation ).isEqualTo( repository.getDirectory() );
  }

  @Test
  public void testComputeWithSharedFile() throws CoreException {
    projectHelper = new ProjectHelper( repository.getWorkTree() );
    IFolder folder = projectHelper.createFolder( "folder" );
    IFile file = projectHelper.createFile( folder, "file", "" );

    File repositoryLocation = computeRepositoryLocation( file );

    assertThat( repositoryLocation ).isEqualTo( repository.getDirectory() );
  }

  @Test
  public void testComputeWithSharedProjectInRepositoryRoot() {
    projectHelper = new ProjectHelper( Path.fromOSString( repository.getWorkTree().toString() ) );

    File repositoryLocation = computeRepositoryLocation( projectHelper.getProject() );

    assertThat( repositoryLocation ).isEqualTo( repository.getDirectory() );
  }

  @Test
  public void testComputeWithSharedFileInEditor() throws CoreException {
    projectHelper = new ProjectHelper( repository.getWorkTree() );
    IFile file = projectHelper.createFile( "file.txt", "" );
    IEditorPart editor = openEditor( file );

    File repositoryLocation = computeRepositoryLocation( editor );

    assertThat( repositoryLocation ).isEqualTo( repository.getDirectory() );
  }

  @Test
  public void testComputeWithUnsharedFileInEditor() throws CoreException {
    projectHelper = new ProjectHelper( tempFolder.getRoot() );
    IFile file = projectHelper.createFile( "file.txt", "" );
    IEditorPart editor = openEditor( file );

    File repositoryLocation = computeRepositoryLocation( editor );

    assertThat( repositoryLocation ).isNull();
  }

  @Before
  public void setUp() throws GitAPIException {
    partHelper = new PartHelper();
    partHelper.hideView( INTRO_VIEW_ID );
    repository = initRepository();
  }

  @After
  public void tearDown() throws CoreException {
    repository.close();
    if( projectHelper != null ) {
      projectHelper.dispose();
    }
    partHelper.closeEditors();
  }

  private Repository initRepository() throws GitAPIException {
    File directory = new File( tempFolder.getRoot(), "repo" );
    return Git.init().setDirectory( directory ).call().getRepository();
  }

  private static File computeRepositoryLocation( IResource... resources ) {
    return new RepositoryLocationComputer( new StructuredSelection( resources ), null ).compute();
  }

  private static File computeRepositoryLocation( IWorkbenchPart activePart ) {
    return new RepositoryLocationComputer( new TextSelection( 0, 0 ), activePart ).compute();
  }

  private static IEditorPart openEditor( IFile file ) throws PartInitException {
    IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    return IDE.openEditor( activePage, file );
  }
}
