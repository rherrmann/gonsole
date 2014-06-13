package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.codeaffine.gonsole.internal.ConsoleOutput.ConsoleWriter;

class CommandExecutor {

  private final File gitDirectory;
  private final ConsoleOutput consoleOutput;

  CommandExecutor( ConsoleOutput consoleOutput, File gitDirectory ) {
    this.consoleOutput = consoleOutput;
    this.gitDirectory = gitDirectory;
  }

  String execute( final CommandInfo commandInfo ) {
    final String[] result = new String[ 1 ];
    consoleOutput.write( new ConsoleWriter() {
      @Override
      public void write( OutputStream outputStream ) throws IOException {
        result[ 0 ] = execute( commandInfo, outputStream );
      }
    } );
    return result[ 0 ];
  }

  private String execute( CommandInfo commandInfo, OutputStream outputStream ) {
    RepositoryContext repositoryContext = new RepositoryContext( gitDirectory ); ;
    try {
      CommandAccessor commandAccessor = new CommandAccessor( commandInfo );
      commandAccessor.init( repositoryContext.getRepository(), outputStream );
      return commandAccessor.execute( commandInfo );
    } finally {
      repositoryContext.dispose();
    }
  }
}