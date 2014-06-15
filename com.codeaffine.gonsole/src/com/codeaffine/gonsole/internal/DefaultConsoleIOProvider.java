package com.codeaffine.gonsole.internal;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;

class DefaultConsoleIOProvider implements ConsoleIOProvider {
  private final IOConsoleOutputStream outputStream;
  private final IOConsoleOutputStream errorStream;
  private final IOConsoleInputStream inputStream;
  private final Charset encoding;

  DefaultConsoleIOProvider( Display display, GitConsole console ) {
    inputStream = console.getInputStream();
    outputStream = console.newOutputStream();
    errorStream = console.newOutputStream();
    encoding = Charset.forName( console.getEncoding() );
    initialize( display );
  }

  private void initialize( Display display ) {
    errorStream.setColor( display.getSystemColor( SWT.COLOR_RED ) );
    inputStream.setColor( display.getSystemColor( SWT.COLOR_BLUE ) );
  }

  @Override
  public OutputStream getOutputStream() {
    return outputStream;
  }

  @Override
  public OutputStream getErrorStream() {
    return errorStream;
  }

  @Override
  public InputStream getInputStream() {
    return inputStream;
  }

  @Override
  public Charset getCharacterEncoding() {
    return encoding;
  }

  @Override
  public String getLineDelimiter() {
    return System.getProperty( "line.separator" );
  }
}