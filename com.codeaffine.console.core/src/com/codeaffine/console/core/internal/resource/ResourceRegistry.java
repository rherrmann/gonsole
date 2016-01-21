package com.codeaffine.console.core.internal.resource;

import static java.util.Objects.requireNonNull;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ResourceRegistry {

  static final MissingImageDescriptor MISSING_IMAGE_DESCRIPTOR = new MissingImageDescriptor();
  static final RGB MISSING_RGB = new RGB( 255, 0, 0 );

  private final LocalResourceManager resourceManager;

  public ResourceRegistry( Display display ) {
    requireNonNull( display );
    this.resourceManager = new LocalResourceManager( JFaceResources.getResources( display ) );
  }

  public Color getColor( RGB colorDefinition ) {
    checkDisposeState();

    Color result = resourceManager.createColor( MISSING_RGB );
    if( colorDefinition != null ) {
      result = resourceManager.createColor( colorDefinition );
    }
    return result;
  }

  public Image getImage( ImageDescriptor imageDescriptor ) {
    checkDisposeState();

    Image result = resourceManager.createImage( MISSING_IMAGE_DESCRIPTOR );
    if( imageDescriptor != null ) {
      result = resourceManager.createImage( imageDescriptor );
    }
    return result;
  }

  public void dispose() {
    resourceManager.dispose();
  }

  private void checkDisposeState() {
    if( resourceManager.getDevice().isDisposed() ) {
      throw new IllegalStateException( "Display has been disposed" );
    }
  }
}