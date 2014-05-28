package com.codeaffine.gonsole.internal;

import java.util.Scanner;

import org.eclipse.ui.console.IOConsole;


public class GitConsole extends IOConsole {

  public GitConsole() {
    super( "Interactive Git Console", null );
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        while( true ) {
          Scanner scanner = new Scanner( getInputStream() );
          String line = scanner.nextLine();
          String[] parts = line.split( " " );
          RepositoryProvider repositoryProvider = new RepositoryProvider();
          repositoryProvider.ensureRepository();
          new GitInterpreter( newOutputStream(), repositoryProvider.getDirectory() ).execute( parts );
        }
      }
    };
    Thread thread = new Thread( runnable );
    thread.setDaemon( true );
    thread.start();
  }

}
