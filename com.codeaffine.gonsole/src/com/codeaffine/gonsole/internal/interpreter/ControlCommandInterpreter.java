package com.codeaffine.gonsole.internal.interpreter;

import java.io.File;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;


public class ControlCommandInterpreter implements ConsoleCommandInterpreter {

  private final ConsoleOutput consoleOutput;
  private final CompositeRepositoryProvider repositoryProvider;

  public ControlCommandInterpreter( ConsoleOutput consoleOutput, CompositeRepositoryProvider repositoryProvider ) {
    this.consoleOutput = consoleOutput;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public boolean isRecognized( String... commandLine ) {
    return commandLine.length > 0 && "use".equals( commandLine[ 0 ] );
  }

  @Override
  public String execute( String... parts ) {
    String result = "Unknown repository";
    if( parts.length == 2 && "use".equals( parts[ 0 ] ) ) {
      String newRepositoryName = parts[ 1 ];
      File repositoryLocation = findRepositoryLocationByName( newRepositoryName );
      if( repositoryLocation != null ) {
        result = null;
        repositoryProvider.setCurrentRepositoryLocation( repositoryLocation );
        String changedRepositoryName = getRepositoryName( repositoryLocation );
        String message = String.format( "Current repository is: %s", changedRepositoryName );
        consoleOutput.writeLine( message );
      }
    }
    return result;
  }

  private File findRepositoryLocationByName( String newRepositoryName ) {
    File result = null;
    for( File repositoryLocation : repositoryProvider.getRepositoryLocations() ) {
      if( newRepositoryName.equals( getRepositoryName( repositoryLocation ) ) ) {
        result = repositoryLocation;
      }
    }
    return result;
  }

  public static String getRepositoryName( File repositoryLocation ) {
    String result = "no repository";
    if( repositoryLocation != null  ) {
      File parentFile = repositoryLocation.getParentFile();
      result = parentFile == null ? null : parentFile.getName();
    }
    return result;
  }

}
