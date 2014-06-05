package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.google.common.base.Throwables;

public class RepositoryProvider {

  private final File directory;

  public RepositoryProvider() {
    try {
      String tmpDir = System.getProperty( "java.io.tmpdir" );
      directory = new File( tmpDir, "gonsole-repository" ).getCanonicalFile();
    } catch( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  public File getGitDirectory() {
    return new File( directory, ".git" );
  }

  public void ensureRepository() {
    if( !directory.exists() ) {
      directory.mkdirs();
      try {
        Git git = Git.init().setDirectory( directory ).setBare( false ).call();
        git.getRepository().close();
      } catch( GitAPIException e ) {
        Throwables.propagate( e );
      }
    }
  }

  public void deleteRepository() {
    if( directory.exists() ) {
      deleteDirectory( directory );
      deleteFile( directory );
    }
  }

  private static void deleteDirectory( File file ) {
    if( file.isDirectory() ) {
      File[] children = file.listFiles();
      for( File child : children ) {
        if( child.exists() ) {
          deleteDirectory( child );
          deleteFile( child );
        }
      }
    }
  }

  private static void deleteFile( File file ) {
    boolean deleted = file.delete();
    if( !deleted ) {
      throw new RuntimeException( "Unable to delete file: " + file );
    }
  }

}
