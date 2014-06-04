package com.codeaffine.gonsole.internal;

import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

class RepositoryContext {

  private final String directory;

  private boolean disposed;
  Repository repository;

  RepositoryContext( String directory ) {
    this.directory = directory;
  }

  Repository getRepository() {
    checkState( !disposed, "Repository has been disposed." );
    if( repository == null ) {
      create();
    }
    return repository;
  }

  void dispose() {
    if( repository != null ) {
      repository.close();
      repository = null;
    }
    disposed = true;
  }

  boolean isDisposed() {
    return disposed;
  }

  private void create() {
    try {
      File gitDir = new File( directory );
      repository = new RepositoryBuilder().setGitDir( gitDir ).setMustExist( true ).build();
    } catch( IOException e ) {
      throw new RuntimeException( e );
    }
  }
}