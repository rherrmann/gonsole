package com.codeaffine.gonsole.internal.repository;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.codeaffine.gonsole.RepositoryProvider;
import com.google.common.base.Throwables;

public class TempRepositoryProvider implements RepositoryProvider {

  private final File[] directories;

  public TempRepositoryProvider() {
    String tmpDir = System.getProperty( "java.io.tmpdir" );
    try {
      directories = new File[] {
        new File( tmpDir, "gonsole-repository-1" ).getCanonicalFile(),
        new File( tmpDir, "gonsole-repository-2" ).getCanonicalFile()
      };
    } catch( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  public void ensureRepositories() {
    for( File directory : directories ) {
      if( !directory.exists() ) {
        directory.mkdirs();
        try {
          Git git = Git.init().setDirectory( directory ).call();
          git.getRepository().close();
        } catch( GitAPIException e ) {
          Throwables.propagate( e );
        }
      }
    }
  }

  @Override
  public File[] getRepositoryLocations() {
    File[] result = new File[ directories.length ];
    for( int i = 0; i < result.length; i++ ) {
      result[ i ] = new File( directories[ i ], ".git" );
    }
    return result;
  }
}
