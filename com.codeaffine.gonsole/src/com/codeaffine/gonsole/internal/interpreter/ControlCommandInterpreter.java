package com.codeaffine.gonsole.internal.interpreter;

import java.io.File;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.util.Repositories;


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
      String newRepository = parts[ 1 ];
      File repositoryLocation = findRepositoryLocationByName( newRepository );
      if( repositoryLocation == null ) {
        repositoryLocation = findRepositoryLocationByPath( newRepository );
      }
      if( repositoryLocation != null ) {
        result = null;
        repositoryProvider.setCurrentRepositoryLocation( repositoryLocation );
        String changedRepositoryName = Repositories.getRepositoryName( repositoryLocation );
        String message = String.format( "Current repository is: %s", changedRepositoryName );
        consoleOutput.writeLine( message );
      }
    }
    return result;
  }

  private File findRepositoryLocationByName( String newRepositoryName ) {
    File result = null;
    for( File repositoryLocation : repositoryProvider.getRepositoryLocations() ) {
      if( newRepositoryName.equals( Repositories.getRepositoryName( repositoryLocation ) ) ) {
        result = repositoryLocation;
      }
    }
    return result;
  }

  private static File findRepositoryLocationByPath( String newRepository ) {
    File result = null;
    File path = new File( newRepository );
    if( path.getName().equals( ".git" ) && path.isDirectory() ) {
      result = path;
    }
    return result;
  }

}
