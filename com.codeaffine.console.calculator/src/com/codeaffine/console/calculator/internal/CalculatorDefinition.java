package com.codeaffine.console.calculator.internal;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ColorScheme;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleDefinition;

public class CalculatorDefinition implements ConsoleDefinition {

  @Override
  public String getTitle() {
    return "Calculator";
  }

  @Override
  public ImageDescriptor getImage() {
    return null;
  }

  @Override
  public ColorScheme getColorScheme() {
    return new CalculatorColorScheme();
  }

  @Override
  public ConsoleComponentFactory getConsoleComponentFactory() {
    return new CalculatorComponentFactory();
  }
}