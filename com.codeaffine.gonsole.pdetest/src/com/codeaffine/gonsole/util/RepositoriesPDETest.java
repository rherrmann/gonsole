package com.codeaffine.gonsole.util;

import static com.codeaffine.gonsole.test.io.Files.toCanonicalFile;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class RepositoriesPDETest {

  private static final boolean BARE = true;
  private static final boolean NON_BARE = false;

  @Rule
  public final TemporaryFolder tempFolder = new TemporaryFolder();

  @Test
  public void testGetWorkDir() throws GitAPIException {
    File repositoryLocation = createRepository( NON_BARE );

    IPath workDir = Repositories.getWorkDir( repositoryLocation );

    assertThat( workDir.toFile() ).isEqualTo( toCanonicalFile( repositoryLocation.getParentFile() ) );
  }

  @Test
  public void testGetWorkDirWithBareRepository() throws GitAPIException {
    File repositoryLocation = createRepository( BARE );

    IPath workDir = Repositories.getWorkDir( repositoryLocation );

    assertThat( workDir ).isNull();
  }

  private File createRepository( boolean bare ) throws GitAPIException {
    Git git = Git.init().setDirectory( tempFolder.getRoot() ).setBare( bare ).call();
    File result = git.getRepository().getDirectory();
    git.getRepository().close();
    return result;
  }
}
