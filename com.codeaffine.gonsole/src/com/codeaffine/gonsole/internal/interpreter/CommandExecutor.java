package com.codeaffine.gonsole.internal.interpreter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsoleOutput.ConsoleWriter;

class CommandExecutor {

  private final File repositoryLocation;
  private final ConsoleOutput consoleOutput;

  CommandExecutor( ConsoleOutput consoleOutput, File repositoryLocation ) {
    this.consoleOutput = consoleOutput;
    this.repositoryLocation = repositoryLocation;
  }

  File getRepositoryLocation() {
    return repositoryLocation;
  }

  String execute( CommandInfo commandInfo ) {
    String[] result = new String[ 1 ];
    consoleOutput.write( new ConsoleWriter() {
      @Override
      public void write( OutputStream outputStream ) throws IOException {
        result[ 0 ] = execute( commandInfo, outputStream );
      }
    } );
    return result[ 0 ];
  }

  private String execute( CommandInfo commandInfo, OutputStream outputStream ) {
    RepositoryContext repositoryContext = new RepositoryContext( repositoryLocation ); ;
    try {
      CommandAccessor commandAccessor = new CommandAccessor( commandInfo );
      commandAccessor.init( repositoryContext.getRepository(), outputStream );
      return commandAccessor.execute();
    } finally {
      repositoryContext.dispose();
    }
  }
}