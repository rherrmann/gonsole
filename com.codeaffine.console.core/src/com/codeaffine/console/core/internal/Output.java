package com.codeaffine.console.core.internal;

import static com.codeaffine.console.core.ConsoleConstants.ENCODING;
import static com.codeaffine.console.core.ConsoleConstants.LINE_DELIMITER;

import java.io.IOException;
import java.io.OutputStream;

import com.codeaffine.console.core.ConsoleOutput;

public class Output implements ConsoleOutput {

  private final OutputStream outputStream;

  public Output( OutputStream outputStream ) {
    this.outputStream = outputStream;
  }

  public static ConsoleOutput create( OutputStream outputStream ) {
    return new Output( outputStream );
  }

  @Override
  public void write( ConsoleWriter consoleWriter ) {
    write( outputStream, consoleWriter );
  }

  @Override
  public void write( String text ) {
    write( new EncodingConsoleWriter( text ) );
  }

  @Override
  public void writeLine( String text ) {
    write( text + LINE_DELIMITER );
  }

  private static void write( OutputStream outputStream, ConsoleWriter consoleWriter ) {
    try {
      consoleWriter.write( outputStream );
    } catch( IOException ioe ) {
      throw new RuntimeException( ioe );
    }
  }

  private static class EncodingConsoleWriter implements ConsoleWriter {
    private final String text;

    EncodingConsoleWriter( String text ) {
      this.text = text;
    }

    @Override
    public void write( OutputStream outputStream ) throws IOException {
      outputStream.write( text.getBytes( ENCODING ) );
    }
  }
}