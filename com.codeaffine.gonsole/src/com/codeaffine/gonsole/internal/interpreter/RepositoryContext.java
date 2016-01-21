package com.codeaffine.gonsole.internal.interpreter;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

class RepositoryContext {

  private final File gitDirectory;
  private boolean disposed;
  Repository repository;

  RepositoryContext( File gitDirectory ) {
    this.gitDirectory = gitDirectory;
  }

  Repository getRepository() {
    checkState();
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
      repository = new RepositoryBuilder().setGitDir( gitDirectory ).setMustExist( true ).build();
    } catch( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  private void checkState() {
    if( disposed ) {
      throw new IllegalStateException( "RepositoryContext has been disposed." );
    }
  }
}