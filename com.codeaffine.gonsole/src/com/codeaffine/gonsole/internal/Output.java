package com.codeaffine.gonsole.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.codeaffine.gonsole.ConsoleOutput;

public class Output implements ConsoleOutput {

  private final OutputStream outputStream;
  private final Charset encoding;
  private final String lineDelimiter;

  public Output( OutputStream outputStream, Charset encoding, String lineDelimiter ) {
    this.outputStream = outputStream;
    this.encoding = encoding;
    this.lineDelimiter = lineDelimiter;
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
    write( text + lineDelimiter );
  }

  private static void write( OutputStream outputStream, ConsoleWriter consoleWriter ) {
    try {
      consoleWriter.write( outputStream );
    } catch( IOException ioe ) {
      throw new RuntimeException( ioe );
    }
  }

  private class EncodingConsoleWriter implements ConsoleWriter {
    private final String text;

    EncodingConsoleWriter( String text ) {
      this.text = text;
    }

    @Override
    public void write( OutputStream outputStream ) throws IOException {
      outputStream.write( text.getBytes( encoding ) );
    }
  }
}