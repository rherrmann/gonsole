package com.codeaffine.console.core;

import org.eclipse.jface.resource.ImageDescriptor;

public interface ConsoleDefinition {
  String getTitle();
  ImageDescriptor getImage();
  ColorScheme getColorScheme();
  ConsoleComponentFactory getConsoleComponentFactory();
}