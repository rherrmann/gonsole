package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.codeaffine.gonsole.internal.ConsoleOutput.ConsoleWriter;
import com.google.common.base.Throwables;

class CommandExecutor {

  private final File gitDirectory;
  private final ConsoleOutput consoleOutput;

  CommandExecutor( ConsoleOutput consoleOutput, File gitDirectory ) {
    this.consoleOutput = consoleOutput;
    this.gitDirectory = gitDirectory;
  }

  void execute( final CommandInfo commandInfo ) {
    consoleOutput.write( new ConsoleWriter() {
      @Override
      public void write( OutputStream outputStream ) throws IOException {
        execute( commandInfo, outputStream );
      }
    } );
  }

  private void execute( CommandInfo commandInfo, OutputStream outputStream ) {
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