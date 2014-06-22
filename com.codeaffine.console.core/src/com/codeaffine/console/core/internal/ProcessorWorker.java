package com.codeaffine.console.core.internal;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.TextConsoleViewer;

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
  private final TextConsoleViewer viewer;
  private final Input consoleInput;

  ProcessorWorker( TextConsoleViewer viewer, ConsoleIoProvider ioProvider, ConsoleComponentFactory factory ) {
    this.consolePromptOutput = Output.create( ioProvider.getPromptStream(), ioProvider );
    this.consoleStandardOutput = Output.create( ioProvider.getOutputStream(), ioProvider );
    this.consoleErrorOutput = Output.create( ioProvider.getErrorStream(), ioProvider );
    this.consoleInput = new Input( ioProvider );
    this.consoleComponentFactory = factory;
    this.viewer = viewer;
  }

  @Override
  public void run() {
    final StyledText textWidget = viewer.getTextWidget();

    textWidget.getDisplay().syncExec( new Runnable() {
      @Override
      public void run() {
      }
    } );

    viewer.getDocument().addDocumentListener( new IDocumentListener() {
      @Override
      public void documentChanged( DocumentEvent evt ) {
        textWidget.setCaretOffset( textWidget.getCharCount() );
      }

      @Override
      public void documentAboutToBeChanged( DocumentEvent event ) {
        // TODO Auto-generated method stub
      }
    } );

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

  }

  private static void printStackTrace( ConsoleOutput consoleOutput, Exception exception ) {
    consoleOutput.write( Throwables.getStackTraceAsString( exception ) );
  }
}