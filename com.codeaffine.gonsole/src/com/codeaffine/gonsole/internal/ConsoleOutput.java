package com.codeaffine.gonsole.internal;

import java.io.IOException;
import java.io.OutputStream;

import com.google.common.io.Closeables;


public class ConsoleOutput {

  public interface ConsoleWriter {
    void write( OutputStream outputStream ) throws IOException;
  }

  private final ConsoleIOProvider consoleIOProvider;

  public ConsoleOutput( ConsoleIOProvider consoleIOProvider ) {
    this.consoleIOProvider = consoleIOProvider;
  }

  public void write( ConsoleWriter consoleWriter ) {
    OutputStream outputStream = consoleIOProvider.newOutputStream();
    try {
      consoleWriter.write( outputStream );
    } catch( IOException ioe ) {
      throw new RuntimeException( ioe );
    } finally {
      Closeables.closeQuietly( outputStream );
    }
  }

  public void write( final String text ) {
    write( new ConsoleWriter() {
      @Override
      public void write( OutputStream outputStream ) throws IOException {
        outputStream.write( text.getBytes( consoleIOProvider.getEncoding() ) );
      }
    } );
  }

}
