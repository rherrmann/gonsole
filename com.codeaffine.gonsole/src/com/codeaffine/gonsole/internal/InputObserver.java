package com.codeaffine.gonsole.internal;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.TextConsoleViewer;

import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.google.common.base.Throwables;


public class InputObserver {

  private final ConsoleOutput consoleOutput;
  private final ConsoleInput consoleInput;
  private final CompositeRepositoryProvider repositoryProvider;
  private final ExecutorService executorService;

  public InputObserver( ConsoleIOProvider consoleIOProvider,
                        CompositeRepositoryProvider repositoryProvider )
  {
    this.consoleOutput = new ConsoleOutput( consoleIOProvider );
    this.consoleInput = new ConsoleInput( consoleIOProvider );
    this.repositoryProvider = repositoryProvider;
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
        public void documentChanged( DocumentEvent event ) {
          textWidget.setCaretOffset( textWidget.getCharCount() );
        }

        @Override
        public void documentAboutToBeChanged( DocumentEvent event ) {
          // TODO Auto-generated method stub
        }
      } );

      String line;
      do {
        File gitDirectory = repositoryProvider.getCurrentRepositoryLocation();
        String repositoryName = ControlCommandInterpreter.getRepositoryName( gitDirectory );

        consoleOutput.write( repositoryName + ">" );

        line = consoleInput.readLine();
        if( line != null && line.trim().length() > 0 ) {
          String[] commandLine = line.split( " " );
          ConsoleCommandInterpreter[] interpreters = {
            new ControlCommandInterpreter( consoleOutput, repositoryProvider ),
            new GitCommandInterpreter( consoleOutput, gitDirectory )
          };
          try {
            boolean commandExecuted = false;
            for( int i = 0; !commandExecuted && i < interpreters.length; i++ ) {
              if( interpreters[ i ].isRecognized( commandLine ) ) {
                String errorOutput = interpreters[ i ].execute( commandLine );
                if( errorOutput != null ) {
                  consoleOutput.write( errorOutput );
                }
                commandExecuted = true;
              }
            }
            if( !commandExecuted ) {
              consoleOutput.write( "Unrecognized command: " + commandLine[ 0 ] + "\n" );
            }
          } catch( Exception exception ) {
            printStackTrace( consoleOutput, exception );
          }
        }
      } while( line != null );
    }

    private void printStackTrace( ConsoleOutput consoleOutput, Exception exception ) {
      consoleOutput.write( Throwables.getStackTraceAsString( exception ) );
    }
  }

}
