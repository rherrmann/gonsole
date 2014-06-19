package com.codeaffine.gonsole.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.TextConsoleViewer;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;
import com.codeaffine.gonsole.internal.resource.ConsoleIoProvider;
import com.google.common.base.Throwables;

public class InputObserver {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final ConsoleOutput consoleStandardOutput;
  private final ConsoleOutput consolePromptOutput;
  private final ConsoleOutput consoleErrorOutput;
  private final Input consoleInput;
  private final ExecutorService executorService;


  public InputObserver( ConsoleIoProvider consoleIoProvider, ConsoleComponentFactory consoleComponentFactory ) {
    this.consoleComponentFactory = consoleComponentFactory;
    this.consolePromptOutput = Output.create( consoleIoProvider.getPromptStream(), consoleIoProvider );
    this.consoleStandardOutput = Output.create( consoleIoProvider.getOutputStream(), consoleIoProvider );
    this.consoleErrorOutput = Output.create( consoleIoProvider.getErrorStream(), consoleIoProvider );
    this.consoleInput = new Input( consoleIoProvider );
    this.executorService = Executors.newSingleThreadExecutor();
  }

  public void start( TextConsoleViewer viewer ) {
    executorService.execute( new InputScanner( viewer ) );
  }

  public void stop() {
    executorService.shutdownNow();
  }


  private class InputScanner implements Runnable {
    private final TextConsoleViewer viewer;

    InputScanner( TextConsoleViewer viewer ) {
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

    private void printStackTrace( ConsoleOutput consoleOutput, Exception exception ) {
      consoleOutput.write( Throwables.getStackTraceAsString( exception ) );
    }
  }

}
