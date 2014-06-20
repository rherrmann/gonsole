package com.codeaffine.console.core.pdetest;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.internal.Output;

public class ConsoleOutputs {

  public static ConsoleOutput create( OutputStream outputStream, Charset encoding, String lineDelimiter ) {
    return Output.create( outputStream, encoding, lineDelimiter );
  }
}
