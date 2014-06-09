package com.codeaffine.gonsole.internal.activator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class IconRegistryPDETest {

  @Test
  @Parameters( source = IconPathProvider.class )
  public void testIconPathDefinition( String iconPath ) {
    ImageDescriptor descriptor = new IconRegistry().getDescriptor( iconPath );

    assertThat( descriptor )
      .describedAs( "No image descriptor registered for: '%s'", iconPath )
      .isNotNull();
  }

  @Test
  @Parameters( source = IconPathProvider.class )
  public void testIconCreation( String iconPath ) {
    ImageDescriptor descriptor = new IconRegistry().getDescriptor( iconPath );

    Image icon = createAndDisposeIcon( descriptor );

    assertThat( icon )
      .describedAs( "Unable to create icon for: '%s'", iconPath )
      .isNotNull();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testConstructorIfPluginIsNotAvailable() {
    new IconRegistry( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testConstructorIfPluginIsNotActive() {
    new IconRegistry( mock( GonsolePlugin.class ) );
  }

  private static Image createAndDisposeIcon( ImageDescriptor descriptor ) {
    Image result = null;
    try {
      if( descriptor != null ) {
        result = descriptor.createImage( false );
      }
    } finally {
      if( result != null ) {
        result.dispose();
      }
    }
    return result;
  }
}