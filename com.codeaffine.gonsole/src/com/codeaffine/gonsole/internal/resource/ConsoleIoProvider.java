package com.codeaffine.gonsole.internal.resource;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.codeaffine.gonsole.internal.IoStreamProvider;

public class ConsoleIoProvider {

  public static final String LINE_DELIMITER = System.getProperty( "line.separator" );

  private final OutputStream outputStream;
  private final OutputStream promptStream;
  private final OutputStream errorStream;
  private final InputStream inputStream;
  private final Charset encoding;

  public ConsoleIoProvider( ColorDefinition definition, IoStreamProvider ioStreamProvider, Charset encoding ) {
    this.inputStream = ioStreamProvider.getInputStream( definition.getInputColor() );
    this.outputStream = ioStreamProvider.newOutputStream( definition.getOutputColor() );
    this.promptStream = ioStreamProvider.newOutputStream( definition.getPromptColor() );
    this.errorStream = ioStreamProvider.newOutputStream( definition.getErrorColor() );
    this.encoding = encoding;
  }

  public OutputStream getOutputStream() {
    return outputStream;
  }

  public OutputStream getPromptStream() {
    return promptStream;
  }

  public OutputStream getErrorStream() {
    return errorStream;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  public String getLineDelimiter() {
    return LINE_DELIMITER;
  }

  public Charset getEncoding() {
    return encoding;
  }
}