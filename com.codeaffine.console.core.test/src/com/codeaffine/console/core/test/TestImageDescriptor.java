package com.codeaffine.console.core.test;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class TestImageDescriptor extends ImageDescriptor {

  @Override
  public ImageData getImageData() {
    return new ImageData( 6, 6, 1, new PaletteData( new RGB[]{ new RGB( 255, 0, 0 ) } ) );
  }
}