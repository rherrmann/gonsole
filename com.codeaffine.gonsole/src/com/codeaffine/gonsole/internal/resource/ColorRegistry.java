package com.codeaffine.gonsole.internal.resource;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Maps;

class ColorRegistry {

  private final Display display;
  private final Map<RGB, Color> colors;

  ColorRegistry( Display display ) {
    checkArgument( display != null, "Parameter 'display' must not be null." );

    this.colors = Maps.newHashMap();
    this.display = display;
    registerDisposeHock( display );
  }

  Color getColor( RGB colorDefinition ) {
    checkState( !display.isDisposed(), "Display has been disposed." );

    Color result = colors.get( colorDefinition );
    if( result == null ) {
      result = ColorDescriptor.createFrom( colorDefinition ).createColor( display );
      colors.put( colorDefinition, result );
    }
    return result;
  }

  void clear() {
    for( Color color : colors.values() ) {
      color.dispose();
    }
    colors.clear();
  }

  private void registerDisposeHock( Display display ) {
    display.disposeExec( new Runnable() {
      @Override
      public void run() {
        clear();
      }
    } );
  }
}