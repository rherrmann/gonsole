package com.codeaffine.console.core.internal;


import java.util.concurrent.atomic.AtomicBoolean;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;
import com.codeaffine.console.core.internal.resource.ConsoleIoProvider;
import com.google.common.base.Throwables;

class ProcessorWorker implements Runnable {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final ConsoleOutput consoleStandardOutput;
  private final ConsoleOutput consolePromptOutput;
  private final ConsoleOutput consoleErrorOutput;
  private final Input consoleInput;
  AtomicBoolean shutdown = new AtomicBoolean();

  ProcessorWorker( ConsoleIoProvider ioProvider, ConsoleComponentFactory factory ) {
    this.consolePromptOutput = Output.create( ioProvider.getPromptStream(), ioProvider );
    this.consoleStandardOutput = Output.create( ioProvider.getOutputStream(), ioProvider );
    this.consoleErrorOutput = Output.create( ioProvider.getErrorStream(), ioProvider );
    this.consoleInput = new Input( ioProvider );
    this.consoleComponentFactory = factory;
  }

  @Override
  public void run() {
    try {
      String line;
      do {
        ConsolePrompt consolePrompt = consoleComponentFactory.createConsolePrompt( consolePromptOutput );
        consolePrompt.writePrompt();

        line = consoleInput.readLine();

        if( line != null && line.trim().length() > 0 ) {
          String[] commandLine = new CommandLineSplitter( line ).split();

          ConsoleCommandInterpreter[] interpreters = consoleComponentFactory.createCommandInterpreters( consoleStandardOutput );
          try {
            boolean commandExecuted = false;
            for( int i = 0; !commandExecuted && i < interpreters.length; i++ ) {
              if( interpreters[ i ].isRecognized( commandLine ) ) {
                String errorOutput = interpreters[ i ].execute( commandLine );
                if( errorOutput != null ) {
                  consoleErrorOutput.writeLine( errorOutput );
                }
                commandExecuted = true;
              }
            }
            if( !commandExecuted ) {
              consoleErrorOutput.writeLine( "Unrecognized command: " + commandLine[ 0 ] );
            }
          } catch( Exception exception ) {
            printStackTrace( consoleErrorOutput, exception );
          }
        }
      } while( line != null );

    } catch( Exception exception ){
      if( !Thread.interrupted() ) {
        Throwables.propagate( exception );
      }
    }
  }

  private static void printStackTrace( ConsoleOutput consoleOutput, Exception exception ) {
    consoleOutput.write( Throwables.getStackTraceAsString( exception ) );
  }
}