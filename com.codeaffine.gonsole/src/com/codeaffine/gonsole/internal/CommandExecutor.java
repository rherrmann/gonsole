package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.OutputStream;

import com.google.common.base.Throwables;

class CommandExecutor {

  private final OutputStream outputStream;
  private final File gitDirectory;

  CommandExecutor( OutputStream outputStream, File gitDirectory ) {
    this.outputStream = outputStream;
    this.gitDirectory = gitDirectory;
  }

  void execute( CommandInfo commandInfo ) {
    RepositoryContext repositoryContext = new RepositoryContext( gitDirectory ); ;
    try {
      CommandAccessor commandAccessor = new CommandAccessor( commandInfo );
      commandAccessor.init( repositoryContext.getRepository(), outputStream );
      execute( commandInfo, commandAccessor );
    } finally {
      repositoryContext.dispose();
    }
  }

  private static void execute( CommandInfo commandInfo, CommandAccessor commandAccessor ) {
    try {
      commandInfo.getCommand().execute( commandInfo.getArguments() );
    } catch( Exception e ) {
      Throwables.propagate( e );
    } finally {
      commandAccessor.flush();
    }
  }
}