package com.codeaffine.console.core.internal.resource;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

class MissingImageDescriptor extends ImageDescriptor {

  @Override
  public ImageData getImageData() {
    return new ImageData( 3, 3, 1, new PaletteData( new RGB[] { new RGB( 255, 0, 0 ) } ) );
  }
}