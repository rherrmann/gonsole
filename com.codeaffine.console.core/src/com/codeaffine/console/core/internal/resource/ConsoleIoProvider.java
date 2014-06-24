package com.codeaffine.console.core.internal.resource;

import java.io.InputStream;
import java.io.OutputStream;

import com.codeaffine.console.core.internal.IoStreamProvider;

public class ConsoleIoProvider {

  private final OutputStream outputStream;
  private final OutputStream promptStream;
  private final OutputStream errorStream;
  private final InputStream inputStream;

  public ConsoleIoProvider( ColorDefinition definition, IoStreamProvider ioStreamProvider ) {
    this.inputStream = ioStreamProvider.getInputStream( definition.getInputColor() );
    this.outputStream = ioStreamProvider.newOutputStream( definition.getOutputColor() );
    this.promptStream = ioStreamProvider.newOutputStream( definition.getPromptColor() );
    this.errorStream = ioStreamProvider.newOutputStream( definition.getErrorColor() );
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
}