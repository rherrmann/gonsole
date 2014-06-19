package com.codeaffine.gonsole.internal;

import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;

public class IoStreamProvider {

  private final Console console;

  public IoStreamProvider( Console console ) {
    this.console = console;
  }

  public OutputStream newOutputStream( Color color ) {
    IOConsoleOutputStream result = console.newOutputStream();
    result.setColor( color );
    return result;
  }

  public InputStream getInputStream( Color color ) {
    IOConsoleInputStream result = console.getInputStream();
    result.setColor( color );
    return result;
  }
}
