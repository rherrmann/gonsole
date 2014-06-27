package com.codeaffine.gonsole.egit.internal;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.RepositoryUtil;

import com.google.common.base.Function;


@SuppressWarnings("restriction")
public class EGitFacade {

  private final RepositoryUtil repositoryUtil;

  public EGitFacade() {
    repositoryUtil = Activator.getDefault().getRepositoryUtil();
  }

  public File[] getConfiguredRepositoryLocations() {
    Iterable<File> files = transform( getConfiguredRepositories(), new ToFileFunction() );
    return toArray( files, File.class );
  }

  private List<String> getConfiguredRepositories() {
    return repositoryUtil.getConfiguredRepositories();
  }

  private static class ToFileFunction implements Function<String, File> {
    @Override
    public File apply( String input ) {
      try {
        return new File( input ).getCanonicalFile();
      } catch( IOException ioe ) {
        throw new RuntimeException( ioe );
      }
    }
  }
}
