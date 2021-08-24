package com.codeaffine.gonsole.egit.internal;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import org.eclipse.egit.core.RepositoryUtil;


@SuppressWarnings("restriction")
public class EGitFacade {

  private final RepositoryUtil repositoryUtil;

  public EGitFacade() {
    repositoryUtil = RepositoryUtilFacade.getRepositoryUtil();
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
      throw new UncheckedIOException( ioe );
    }
  }
}
