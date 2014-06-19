package com.codeaffine.gonsole.internal.gitconsole.interpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.codeaffine.gonsole.internal.gitconsole.interpreter.RepositoryContext;
public class RepositoryContextPDETest {

  @Rule
  public final TemporaryFolder temporaryFolder = new TemporaryFolder();

  private RepositoryContext repositoryContext;
  private File gitDirectory;

  @Test
  public void testGetRepository() {
    Repository actual = repositoryContext.getRepository();

    assertThat( actual.getDirectory() ).isEqualTo( gitDirectory );
  }

  @Test
  public void testGetRepositoryTwice() {
    Repository repo1 = repositoryContext.getRepository();
    Repository repo2 = repositoryContext.getRepository();

    assertThat( repo1 ).isSameAs( repo2 );
  }

  @Test
  public void testDispose() {
    Repository repository = mock( Repository.class );
    repositoryContext.repository = repository;

    repositoryContext.dispose();

    verify( repository ).close();
    assertThat( repositoryContext.isDisposed() ).isTrue();
  }

  @Test
  public void testDisposeTwice() {
    repositoryContext.dispose();
    repositoryContext.dispose();

    assertThat( repositoryContext.isDisposed() ).isTrue();
  }

  @Test
  public void testGetRepositoryAfterDispose() {
    repositoryContext.getRepository();
    repositoryContext.dispose();

    try {
      repositoryContext.getRepository();
      fail();
    } catch( IllegalStateException expected ) {
    }
  }

  @Test
  public void testDisposeBeforeGetRepository() {
    repositoryContext.dispose();

    assertThat( repositoryContext.repository ).isNull();
  }

  @Test
  public void testIsDisposed() {
    assertThat( repositoryContext.isDisposed() ).isFalse();
  }

  @Test
  public void testProblemOnCreate() {
    try {
      new RepositoryContext( new File( "does/not/exist" ) ).getRepository();
      fail();
    } catch( Exception expected ) {
      assertThat( expected.getCause() ).isInstanceOf( RepositoryNotFoundException.class );
    }
  }

  @Before
  public void setUp() throws Exception {
    gitDirectory = createRepository();
    repositoryContext = new RepositoryContext( gitDirectory );
  }

  @After
  public void tearDown() {
    repositoryContext.dispose();
  }

  private File createRepository() throws GitAPIException, IOException {
    Git git = Git.init().setDirectory( getWorkDirectory() ).setBare( false ).call();
    Repository repository = git.getRepository();
    File result = repository.getDirectory().getCanonicalFile();
    repository.close();
    return result;
  }

  private File getWorkDirectory() throws IOException {
    return temporaryFolder.getRoot().getCanonicalFile();
  }
}

