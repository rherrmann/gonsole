package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;

import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.google.common.base.Charsets;

public class GitConsole extends IOConsole {

  private static final String TYPE = "com.codeaffine.gonsole.console";
  private static final String ENCODING = Charsets.UTF_8.name();

  private final CompositeRepositoryProvider repositoryProvider;
  private final ExecutorService executorService;

  public GitConsole( CompositeRepositoryProvider repositoryProvider ) {
    super( "Interactive Git Console", TYPE, new IconRegistry().getDescriptor( IconRegistry.GONSOLE ), ENCODING, true );
    this.repositoryProvider = repositoryProvider;
    this.executorService = Executors.newSingleThreadExecutor();
    executorService.execute( new InputScanner() );
  }

  private class InputScanner implements Runnable {

    private final Scanner scanner;

    InputScanner() {
      scanner = new Scanner( getInputStream() );
    }

    @Override
    public void run() {
      try {
        while( true ) {
          String line = scanner.nextLine();
          String[] parts = line.split( " " );
          File gitDirectory = repositoryProvider.getCurrentRepositoryLocation();
          IOConsoleOutputStream outputStream = newOutputStream();
          try {
            if( !new ConsoleCommandInterpreter( outputStream, repositoryProvider ).execute( parts ) ) {
              new GitInterpreter( outputStream, gitDirectory ).execute( parts );
            }
          } catch( Exception exception ) {
            printStackTrace( outputStream, exception );
          } finally {
            try {
              outputStream.close();
            } catch( IOException ignore ) {
            }
          }
        }
      } catch( NoSuchElementException e ) {
        if( !executorService.isShutdown() ) {
          e.printStackTrace();
        }
      }
    }

    private void printStackTrace( OutputStream outputStream, Exception exception ) {
      OutputStreamWriter streamWriter = new OutputStreamWriter( outputStream, Charsets.UTF_8 );
      PrintWriter printWriter = new PrintWriter( streamWriter, true );
      exception.printStackTrace( printWriter );
      printWriter.flush();
      printWriter.close();
    }
  }

  @Override
  protected void dispose() {
    executorService.shutdownNow();
    super.dispose();
  }
}