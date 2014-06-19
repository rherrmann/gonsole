package com.codeaffine.gonsole.internal;

import java.io.File;

import com.codeaffine.gonsole.ConsoleCommandInterpreter;
import com.codeaffine.gonsole.ConsoleOutput;
import com.codeaffine.gonsole.internal.gitconsole.repository.CompositeRepositoryProvider;


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
    String result = "Unkown repository";
    if( parts.length == 2 && parts[ 0 ].equals( "use" ) ) {
      String newRepositoryName = parts[ 1 ];
      File[] repositoryLocations = repositoryProvider.getRepositoryLocations();
      for( File repositoryLocation : repositoryLocations ) {
        if( newRepositoryName.equals( getRepositoryName( repositoryLocation ) ) ) {
          repositoryProvider.setCurrentRepositoryLocation( repositoryLocation );
          result = null;
        }
      }
      String changedRepositoryName = getRepositoryName( repositoryProvider.getCurrentRepositoryLocation() );
      String message = String.format( "Current repository is: %s", changedRepositoryName );
      consoleOutput.writeLine( message );
    }
    return result;
  }

  public static String getRepositoryName( File repositoryLocation ) {
    File parentFile = repositoryLocation.getParentFile();
    return parentFile == null ? null : parentFile.getName();
  }

}
