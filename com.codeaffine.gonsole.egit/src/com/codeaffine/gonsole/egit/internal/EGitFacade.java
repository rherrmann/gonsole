package com.codeaffine.gonsole.egit.internal;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.RepositoryUtil;


@SuppressWarnings("restriction")
public class EGitFacade {

  private final RepositoryUtil repositoryUtil;

  public EGitFacade() {
    repositoryUtil = Activator.getDefault().getRepositoryUtil();
  }

  public File[] getConfiguredRepositoryLocations() {
    return getConfiguredRepositories().stream().map( EGitFacade::toFile ).toArray( File[]::new );
  }

  private List<String> getConfiguredRepositories() {
    return repositoryUtil.getConfiguredRepositories();
  }

  private static File toFile( String path ) {
    try {
      return new File( path ).getCanonicalFile();
    } catch( IOException ioe ) {
      throw new RuntimeException( ioe );
    }
  }
}
