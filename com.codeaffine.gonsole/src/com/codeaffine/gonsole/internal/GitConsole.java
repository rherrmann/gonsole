package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;


public class GitConsole extends IOConsole {

  private final RepositoryProvider repositoryProvider;

  public GitConsole() {
    super( "Interactive Git Console", null );
    repositoryProvider = new RepositoryProvider();
    repositoryProvider.ensureRepositories();
    repositoryProvider.setCurrentGitDirectory( repositoryProvider.getGitDirectories()[ 0 ] );
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        while( true ) {
          Scanner scanner = new Scanner( getInputStream() );
          String line = scanner.nextLine();
          String[] parts = line.split( " " );
          File gitDirectory = repositoryProvider.getCurrentGitDirectory();
          IOConsoleOutputStream outputStream = newOutputStream();
          try {
            if( !new ConsoleCommandInterpreter( outputStream, repositoryProvider ).execute( parts ) ) {
              new GitInterpreter( outputStream, gitDirectory ).execute( parts );
            }
          } finally {
            try {
              outputStream.close();
            } catch( IOException ignore ) {
            }
          }
        }
      }
    };
    Thread thread = new Thread( runnable );
    thread.setDaemon( true );
    thread.start();
  }

}
