package com.codeaffine.gonsole.internal;

import java.io.File;

import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;


public class ConsoleCommandInterpreter {

  private final ConsoleOutput consoleOutput;
  private final CompositeRepositoryProvider repositoryProvider;

  public ConsoleCommandInterpreter( ConsoleOutput consoleOutput, CompositeRepositoryProvider repositoryProvider ) {
    this.consoleOutput = consoleOutput;
    this.repositoryProvider = repositoryProvider;
  }

  public boolean execute( String[] parts ) {
    boolean result = false;
    if( parts.length == 2 && parts[ 0 ].equals( "cr" ) ) {
      result = true;
      String newRepositoryName = parts[ 1 ];
      File[] repositoryLocations = repositoryProvider.getRepositoryLocations();
      for( File repositoryLocation : repositoryLocations ) {
        if( newRepositoryName.equals( getRepositoryName( repositoryLocation ) ) ) {
          repositoryProvider.setCurrentRepositoryLocation( repositoryLocation );
        }
      }
      String changedRepositoryName = getRepositoryName( repositoryProvider.getCurrentRepositoryLocation() );
      String message = String.format( "Current repository is: %s%n", changedRepositoryName );
      consoleOutput.write( message );
    }
    return result;
  }

  public static String getRepositoryName( File repositoryLocation ) {
    File parentFile = repositoryLocation.getParentFile();
    return parentFile == null ? null : parentFile.getName();
  }

}
