package com.codeaffine.gonsole.internal;

import static com.codeaffine.gonsole.internal.Constants.ERROR_COLOR;
import static com.codeaffine.gonsole.internal.Constants.INPUT_COLOR;
import static com.codeaffine.gonsole.internal.Constants.OUTPUT_COLOR;
import static com.codeaffine.gonsole.internal.Constants.PROMPT_COLOR;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.codeaffine.console.core.ColorScheme;

class DefaultColorScheme implements ColorScheme {

  private final RGB inputColor;
  private final RGB outputColor;
  private final RGB promptColor;
  private final RGB errorColor;

  DefaultColorScheme( Display display ) {
    inputColor = display.getSystemColor( INPUT_COLOR ).getRGB();
    outputColor = display.getSystemColor( OUTPUT_COLOR ).getRGB();
    promptColor = display.getSystemColor( PROMPT_COLOR ).getRGB();
    errorColor = display.getSystemColor( ERROR_COLOR ).getRGB();
  }

  @Override
  public RGB getInputColor() {
    return inputColor;
  }

  @Override
  public RGB getOutputColor() {
    return outputColor;
  }

  @Override
  public RGB getPromptColor() {
    return promptColor;
  }

  @Override
  public RGB getErrorColor() {
    return errorColor;
  }
}