package com.codeaffine.gonsole.egit.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.RepositoryUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


@SuppressWarnings("restriction")
public class EGitRepositoryProviderPDETest {

  @Rule
  public final TemporaryFolder tempFolder = new TemporaryFolder();

  private RepositoryUtil repositoryUtil;

  @Test
  public void testGetRepositoryLocations() throws Exception {
    File repositoryDir = createRepository( tempFolder.getRoot() );
    repositoryUtil.addConfiguredRepository( repositoryDir );

    File[] repositoryLocations = new EGitRepositoryProvider().getRepositoryLocations();

    assertThat( repositoryLocations ).contains( repositoryDir );
  }

  @Before
  public void setUp() {
    repositoryUtil = Activator.getDefault().getRepositoryUtil();
  }

  @After
  public void tearDown() {
    List<String> repositories = repositoryUtil.getConfiguredRepositories();
    for( String repository : repositories ) {
      repositoryUtil.removeDir( new File( repository ) );
    }
  }

  private static File createRepository( File file ) throws GitAPIException, IOException {
    Git git = Git.init().setDirectory( file ).call();
    File result = git.getRepository().getDirectory();
    git.getRepository().close();
    return result.getCanonicalFile();
  }
}