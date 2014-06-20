package com.codeaffine.console.core.internal.resource;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.codeaffine.console.core.ColorScheme;
import com.google.common.base.Preconditions;

public class ColorDefinition {

  private final ColorRegistry colorRegistry;
  private final ColorScheme colorScheme;

  private boolean disposed;

  public ColorDefinition( ColorScheme colorScheme ) {
    this( colorScheme, new ColorRegistry( Display.getCurrent() ) );
  }

  ColorDefinition( ColorScheme colorScheme, ColorRegistry colorRegistry ) {
    this.colorScheme = colorScheme;
    this.colorRegistry = colorRegistry;
  }

  public Color getInputColor() {
    checkDisposed();
    return colorRegistry.getColor( colorScheme.getInputColor() );
  }

  public Color getOutputColor() {
    checkDisposed();
    return colorRegistry.getColor( colorScheme.getOutputColor() );
  }

  public Color getPromptColor() {
    checkDisposed();
    return colorRegistry.getColor( colorScheme.getPromptColor() );
  }

  public Color getErrorColor() {
    checkDisposed();
    return colorRegistry.getColor( colorScheme.getErrorColor() );
  }

  public void dispose() {
    colorRegistry.clear();
    disposed = true;
  }

  void checkDisposed() {
    Preconditions.checkState( !disposed, "ColorDefinition has been disposed." );
  }
}