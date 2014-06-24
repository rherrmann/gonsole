package com.codeaffine.console.core.pdetest.console;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ColorScheme;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleDefinition;
import com.codeaffine.console.core.pdetest.TestImageDescriptor;

public class TestConsoleDefinition implements ConsoleDefinition {

  public static ImageDescriptor IMAGE = new TestImageDescriptor();

  @Override
  public String getTitle() {
    return "Test Console";
  }

  @Override
  public ImageDescriptor getImage() {
    return IMAGE;
  }

  @Override
  public ColorScheme getColorScheme() {
    return new TestColorScheme();
  }

  @Override
  public ConsoleComponentFactory getConsoleComponentFactory() {
    return new TestConsoleComponentFactory();
  }
}