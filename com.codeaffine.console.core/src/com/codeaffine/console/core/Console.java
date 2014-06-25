package com.codeaffine.console.core;

import org.eclipse.jface.resource.ImageDescriptor;


public interface Console {
  void setTitle( String title );
  void setImageDescriptor( ImageDescriptor imageDescriptor );
  void setColorScheme( ColorScheme colorScheme );
  void setConsoleComponentFactory( ConsoleComponentFactory consoleComponentFactory );
  boolean isDisposed();
}
