package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.codeaffine.gonsole.internal.repository.TempRepositoryProvider;
import com.google.common.base.Charsets;


public class ConsoleCommandInterpreter {

  private final OutputStream outputStream;
  private final TempRepositoryProvider repositoryProvider;

  public ConsoleCommandInterpreter( OutputStream outStream, TempRepositoryProvider repoProvider ) {
    this.outputStream = outStream;
    this.repositoryProvider = repoProvider;
  }

  public boolean execute( String[] parts ) {
    boolean result = false;
    if( parts.length == 2 && parts[ 0 ].equals( "cr" ) ) {
      result = true;
      String newRepositoryName = parts[ 1 ];
      File[] gitDirectories = repositoryProvider.getGitDirectories();
      for( File gitDirectory : gitDirectories ) {
        if( newRepositoryName.equals( gitDirectory.getParent() ) ) {
          repositoryProvider.setCurrentGitDirectory( gitDirectory );
        }
      }
      String message = String.format( "Current repository is: %s%n", newRepositoryName );
      try {
        outputStream.write( message.getBytes( Charsets.UTF_8 ) );
      } catch( IOException e ) {
        throw new RuntimeException( e );
      }
    }
    return result;
  }
}
