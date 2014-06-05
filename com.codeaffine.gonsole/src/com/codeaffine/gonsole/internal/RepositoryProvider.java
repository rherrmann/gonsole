package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.google.common.base.Throwables;

public class RepositoryProvider {

  private final File[] directories;
  private File currentGitDirectory;

  public RepositoryProvider() {
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

  public void deleteRepositories() {
    for( File directory : directories ) {
      if( directory.exists() ) {
        deleteDirectory( directory );
        deleteFile( directory );
      }
    }
  }

  public File[] getGitDirectories() {
    File[] result = new File[ directories.length ];
    for( int i = 0; i < result.length; i++ ) {
      result[ i ] = new File( directories[ i ], ".git" );
    }
    return result;
  }

  public File getCurrentGitDirectory() {
    return currentGitDirectory;
  }

  public void setCurrentGitDirectory( File currentGitDirectory ) {
    this.currentGitDirectory = currentGitDirectory;
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
