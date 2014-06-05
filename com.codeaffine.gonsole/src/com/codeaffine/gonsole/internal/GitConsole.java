package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;

import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.google.common.base.Charsets;

public class GitConsole extends IOConsole {

  private static final String TYPE = "com.codeaffine.gonsole.console";
  private static final String ENCODING = Charsets.UTF_8.name();

  private final CompositeRepositoryProvider repositoryProvider;

  public GitConsole( CompositeRepositoryProvider repositoryProvider ) {
    super( "Interactive Git Console", TYPE, Icons.getDescriptor( Icons.GONSOLE ), ENCODING, true );
    this.repositoryProvider = repositoryProvider;
    Runnable runnable = new InputScanner();
    Thread thread = new Thread( runnable );
    thread.setDaemon( true );
    thread.start();
  }

  private class InputScanner implements Runnable {
    @Override
    public void run() {
      while( true ) {
        Scanner scanner = new Scanner( getInputStream() );
        String line = scanner.nextLine();
        String[] parts = line.split( " " );
        File gitDirectory = repositoryProvider.getCurrentRepositoryLocation();
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
  }

}
