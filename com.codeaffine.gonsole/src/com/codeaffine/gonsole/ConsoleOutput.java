package com.codeaffine.gonsole;

import java.io.IOException;
import java.io.OutputStream;

public interface ConsoleOutput {

  public interface ConsoleWriter {
    void write( OutputStream outputStream ) throws IOException;
  }

  void write( ConsoleWriter consoleWriter );
  void write( String text );
  void writeLine( String text );
}