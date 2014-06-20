package com.codeaffine.console.core.pdetest.console;

import java.nio.charset.Charset;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ColorScheme;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleDefinition;
import com.codeaffine.console.core.pdetest.TestImageDescriptor;
import com.google.common.base.Charsets;

public class TestConsoleDefinition implements ConsoleDefinition {

  public static ImageDescriptor IMAGE = new TestImageDescriptor();

  @Override
  public String getType() {
    return "test.console";
  }

  @Override
  public String getTitle() {
    return "Test Console";
  }

  @Override
  public ImageDescriptor getImage() {
    return IMAGE;
  }

  @Override
  public Charset getEncoding() {
    return Charsets.UTF_8;
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