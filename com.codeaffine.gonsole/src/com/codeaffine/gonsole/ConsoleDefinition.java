package com.codeaffine.gonsole;

import java.nio.charset.Charset;

import org.eclipse.jface.resource.ImageDescriptor;

public interface ConsoleDefinition {
  String getTitle();
  ImageDescriptor getImage();
  String getType();
  Charset getEncoding();
  ColorScheme getColorScheme();
  ConsoleComponentFactory getConsoleComponentFactory();
}