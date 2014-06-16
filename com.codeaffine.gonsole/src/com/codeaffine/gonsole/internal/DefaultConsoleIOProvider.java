package com.codeaffine.gonsole.internal;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;

public class DefaultConsoleIOProvider implements ConsoleIOProvider {
  public static final int INPUT_COLOR = SWT.COLOR_BLUE;
  public static final int OUTPUT_COLOR = SWT.COLOR_WIDGET_FOREGROUND;
  public static final int PROMPT_COLOR = SWT.COLOR_DARK_GRAY;
  public static final int ERROR_COLOR = SWT.COLOR_RED;

  private final IOConsoleOutputStream outputStream;
  private final IOConsoleOutputStream promptStream;
  private final IOConsoleOutputStream errorStream;
  private final IOConsoleInputStream inputStream;
  private final Charset encoding;

  DefaultConsoleIOProvider( Display display, GitConsole console ) {
    inputStream = console.getInputStream();
    outputStream = console.newOutputStream();
    promptStream = console.newOutputStream();
    errorStream = console.newOutputStream();
    encoding = Charset.forName( console.getEncoding() );
    initialize( display );
  }

  private void initialize( Display display ) {
    inputStream.setColor( display.getSystemColor( INPUT_COLOR ) );
    outputStream.setColor( display.getSystemColor( OUTPUT_COLOR ) );
    promptStream.setColor( display.getSystemColor( PROMPT_COLOR ) );
    errorStream.setColor( display.getSystemColor( ERROR_COLOR ) );
  }

  @Override
  public OutputStream getOutputStream() {
    return outputStream;
  }

  @Override
  public OutputStream getPromptStream() {
    return promptStream;
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