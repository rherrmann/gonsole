package com.codeaffine.console.calculator.internal;

import org.eclipse.swt.graphics.RGB;

import com.codeaffine.console.core.ColorScheme;

class CalculatorColorScheme implements ColorScheme {

  @Override
  public RGB getPromptColor() {
    return new RGB( 0, 64, 0 );
  }

  @Override
  public RGB getOutputColor() {
    return new RGB( 0, 0, 0 );
  }

  @Override
  public RGB getInputColor() {
    return new RGB( 0, 0, 255 );
  }

  @Override
  public RGB getErrorColor() {
    return new RGB( 255, 0 , 0 );
  }
}