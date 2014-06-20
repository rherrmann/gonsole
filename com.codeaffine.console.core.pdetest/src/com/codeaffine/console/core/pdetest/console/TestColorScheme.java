package com.codeaffine.console.core.pdetest.console;

import org.eclipse.swt.graphics.RGB;

import com.codeaffine.console.core.ColorScheme;

class TestColorScheme implements ColorScheme {

  @Override
  public RGB getPromptColor() {
    return new RGB( 128, 128, 128 );
  }

  @Override
  public RGB getOutputColor() {
    return new RGB( 255, 255, 255 );
  }

  @Override
  public RGB getInputColor() {
    return new RGB( 0, 0, 255 );
  }

  @Override
  public RGB getErrorColor() {
    return new RGB( 255, 0, 0 );
  }
}