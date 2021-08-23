package com.codeaffine.gonsole.egit.pdetest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.egit.core.GitCorePreferences;
import org.eclipse.egit.core.RepositoryUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

import com.codeaffine.gonsole.egit.internal.RepositoryUtilFacade;


@SuppressWarnings("restriction")
public class EGitRepositoryHelper extends ExternalResource {

  private static final String EGIT_CORE_PLUGIN_ID = "org.eclipse.egit.core";

  private final RepositoryUtil repositoryUtil;
  private final TemporaryFolder tempFolder;

  public EGitRepositoryHelper() {
    repositoryUtil = RepositoryUtilFacade.getRepositoryUtil();
    tempFolder = new TemporaryFolder();
  }

  public File createRepository( String directory ) {
    Git git = initRepository( directory );
    File result = git.getRepository().getDirectory();
    git.getRepository().close();
    return result;
  }

  public File createRegisteredRepository( String directory ) {
    File result = createRepository( directory );
    registerRepository( result );
    return result;
  }

  public void registerRepository( File repositoryLocation ) {
    repositoryUtil.addConfiguredRepository( repositoryLocation );
  }

  private Git initRepository( String directory ) {
    try {
      return Git.init().setDirectory( new File( tempFolder.getRoot(), directory ) ).call();
    } catch( GitAPIException gae ) {
      throw new RuntimeException( gae );
    }
  }

  @Override
  protected void before() throws IOException {
    disableAutoShare();
    tempFolder.create();
  }

  @Override
  protected void after() {
    deregisterRepositories();
    tempFolder.delete();
  }

  private void deregisterRepositories() {
    List<String> repositories = repositoryUtil.getConfiguredRepositories();
    for( String repository : repositories ) {
      repositoryUtil.removeDir( new File( repository ) );
    }
  }

  private static void disableAutoShare() {
    getDefaultPreferencesNode().putBoolean( GitCorePreferences.core_autoShareProjects, false );
    getInstancePreferencesNode().putBoolean( GitCorePreferences.core_autoShareProjects, false );
  }

  private static IEclipsePreferences getInstancePreferencesNode() {
    return InstanceScope.INSTANCE.getNode( EGIT_CORE_PLUGIN_ID );
  }

  private static IEclipsePreferences getDefaultPreferencesNode() {
    return DefaultScope.INSTANCE.getNode( EGIT_CORE_PLUGIN_ID );
  }
}
