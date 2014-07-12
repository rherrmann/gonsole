package com.codeaffine.console.core.internal.resource;

import java.io.InputStream;
import java.io.OutputStream;

import com.codeaffine.console.core.internal.IoStreamProvider;

public class ConsoleIoProvider {

  private final OutputStream outputStream;
  private final OutputStream promptStream;
  private final OutputStream errorStream;
  private final InputStream inputStream;

  public ConsoleIoProvider( ColorDefinition colorDefinition, IoStreamProvider ioStreamProvider ) {
    this.inputStream = ioStreamProvider.getInputStream( colorDefinition.getInputColor() );
    this.outputStream = ioStreamProvider.newOutputStream( colorDefinition.getOutputColor() );
    this.promptStream = ioStreamProvider.newOutputStream( colorDefinition.getPromptColor() );
    this.errorStream = ioStreamProvider.newOutputStream( colorDefinition.getErrorColor() );
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