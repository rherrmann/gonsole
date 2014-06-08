package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.google.common.base.Charsets;


public class ConsoleCommandInterpreter {

  private final OutputStream outputStream;
  private final CompositeRepositoryProvider repositoryProvider;

  public ConsoleCommandInterpreter( OutputStream outputStream, CompositeRepositoryProvider repositoryProvider ) {
    this.outputStream = outputStream;
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
      try {
        outputStream.write( message.getBytes( Charsets.UTF_8 ) );
      } catch( IOException e ) {
        throw new RuntimeException( e );
      }
    }
    return result;
  }

  private static String getRepositoryName( File repositoryLocation ) {
    File parentFile = repositoryLocation.getParentFile();
    return parentFile == null ? null : parentFile.getName();
  }

}
