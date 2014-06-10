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

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.console.TextConsoleViewer;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;

import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.google.common.base.Charsets;

public class GitConsole extends IOConsole {

  private static final String TYPE = "com.codeaffine.gonsole.console";
  private static final String ENCODING = Charsets.UTF_8.name();

  private final CompositeRepositoryProvider repositoryProvider;
  private final ExecutorService executorService;
  private volatile TextConsolePage consolePage;

  public GitConsole( CompositeRepositoryProvider repositoryProvider ) {
    super( "Interactive Git Console", TYPE, new IconRegistry().getDescriptor( IconRegistry.GONSOLE ), ENCODING, true );
    this.repositoryProvider = repositoryProvider;
    this.executorService = Executors.newSingleThreadExecutor();
  }

  @Override
  public IPageBookViewPage createPage( IConsoleView view ) {
    consolePage = ( TextConsolePage )super.createPage( view );
    return new IPageBookViewPage() {

      @Override
      public void setFocus() {
        consolePage.setFocus();
      }

      @Override
      public void setActionBars( IActionBars actionBars ) {
        consolePage.setActionBars( actionBars );
      }

      @Override
      public Control getControl() {
        return consolePage.getControl();
      }

      @Override
      public void dispose() {
        consolePage.dispose();
      }

      @Override
      public void createControl( Composite parent ) {
        consolePage.createControl( parent );
        executorService.execute( new InputScanner() );
      }

      @Override
      public void init( IPageSite site ) throws PartInitException {
        consolePage.init( site );
      }

      @Override
      public IPageSite getSite() {
        return consolePage.getSite();
      }
    };
  }

  public TextConsolePage getPage() {
    return consolePage;
  }

  private class InputScanner implements Runnable {

    private final Scanner scanner;

    InputScanner() {
      scanner = new Scanner( getInputStream() );
    }

    @Override
    public void run() {

      TextConsoleViewer viewer = consolePage.getViewer();
      final StyledText textWidget = viewer.getTextWidget();
      textWidget.getDisplay().syncExec( new Runnable() {
        @Override
        public void run() {
          textWidget.addKeyListener( new KeyListener() {
            @Override
            public void keyReleased( KeyEvent e ) {
              // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed( KeyEvent e ) {
      System.out.println( "caret: " + textWidget.getCaretOffset() );
            }
          } );
        }
      } );

      viewer.getDocument().addDocumentListener( new IDocumentListener() {
        @Override
        public void documentChanged( DocumentEvent event ) {
System.out.println( event.fText + " " + textWidget.getCaretOffset() + " " + textWidget.getCharCount() );
          textWidget.setCaretOffset( textWidget.getCharCount() );
        }

        @Override
        public void documentAboutToBeChanged( DocumentEvent event ) {
          // TODO Auto-generated method stub
        }
      } );

      try {
        while( true ) {
          File gitDirectory = repositoryProvider.getCurrentRepositoryLocation();
          String repositoryName = ConsoleCommandInterpreter.getRepositoryName( gitDirectory );

          IOConsoleOutputStream out = newOutputStream();
          try {
            out.write( ( repositoryName + ">" ).getBytes( Charsets.UTF_8 ) );
          } catch( IOException exception ) {
            printStackTrace( out, exception );
          } finally {
            try {
              out.close();
            } catch( IOException ignore ) {
            }
          }

          String line = scanner.nextLine();
          String[] parts = line.split( " " );
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