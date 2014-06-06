package com.codeaffine.gonsole.egit.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.RepositoryUtil;

import com.codeaffine.gonsole.RepositoryProvider;


@SuppressWarnings("restriction")
public class EGitRepositoryProvider implements RepositoryProvider {

  @Override
  public File[] getRepositoryLocations() {
    String[] repositories = getConfiguredRepositories();
    return toRepositoryLocations( repositories );
  }

  private static String[] getConfiguredRepositories() {
    RepositoryUtil repositoryUtil = Activator.getDefault().getRepositoryUtil();
    List<String> configuredRepositories = repositoryUtil.getConfiguredRepositories();
    return configuredRepositories.toArray( new String[ configuredRepositories.size() ] );
  }

  private static File[] toRepositoryLocations( String[] repositories ) {
    Collection<File> repositoryLocations = new ArrayList<File>();
    for( String repository : repositories ) {
      repositoryLocations.add( new File( repository ) );
    }
    return repositoryLocations.toArray( new File[ repositoryLocations.size() ] );
  }

}
