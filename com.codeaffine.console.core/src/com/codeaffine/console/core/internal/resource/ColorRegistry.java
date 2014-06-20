package com.codeaffine.console.core.internal.resource;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

class ColorRegistry {

  private final LocalResourceManager resourceManager;

  ColorRegistry( Display display ) {
    checkArgument( display != null, "Parameter 'display' must not be null." );

    this.resourceManager = new LocalResourceManager( JFaceResources.getResources( display ) );
  }

  Color getColor( RGB colorDefinition ) {
    checkState( !resourceManager.getDevice().isDisposed(), "Display has been disposed." );

    return resourceManager.createColor( colorDefinition );
  }

  void clear() {
    resourceManager.dispose();
  }
}