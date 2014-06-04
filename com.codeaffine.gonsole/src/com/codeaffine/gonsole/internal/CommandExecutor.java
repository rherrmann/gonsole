package com.codeaffine.gonsole.internal;

import java.io.OutputStream;

import com.google.common.base.Throwables;

class CommandExecutor {

  private final OutputStream outputStream;
  private final RepositoryContext repositoryContext;

  public CommandExecutor( OutputStream outputStream, String directory ) {
    this.outputStream = outputStream;
    this.repositoryContext = new RepositoryContext( directory ); ;
  }

  public void execute( CommandInfo commandInfo ) {
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