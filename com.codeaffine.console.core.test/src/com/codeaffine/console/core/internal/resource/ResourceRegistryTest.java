package com.codeaffine.console.core.internal.resource;

import static com.codeaffine.console.core.internal.resource.ResourceRegistry.MISSING_IMAGE_DESCRIPTOR;
import static com.codeaffine.console.core.internal.resource.ResourceRegistry.MISSING_RGB;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.test.TestImageDescriptor;
import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class ResourceRegistryTest {

  private static final RGB RGB_BLUE = new RGB( 0, 0, 255 );
  private static final TestImageDescriptor IMAGE_DESCRIPTOR = new TestImageDescriptor();

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ResourceRegistry resourceRegistry;

  @Test
  public void testGetColor() {
    Color actual = resourceRegistry.getColor( RGB_BLUE );

    assertThat( actual.getRGB() ).isEqualTo( RGB_BLUE );
    assertThat( actual.isDisposed() ).isFalse();
  }

  @Test
  public void testGetColorWithNullAsRGB() {
    Color actual = resourceRegistry.getColor( null );

    assertThat( actual.getRGB() ).isEqualTo( MISSING_RGB );
  }

  @Test
  public void testGetColorTwice() {
    Color first = resourceRegistry.getColor( RGB_BLUE );
    Color second = resourceRegistry.getColor( RGB_BLUE );

    assertThat( first ).isSameAs( second );
  }

  @Test( expected = IllegalStateException.class )
  public void testGetColorIfDisplayIsDisposed() {
    displayHelper.dispose();

    resourceRegistry.getColor( RGB_BLUE );
  }

  @Test
  public void testGetImage() {
    Image actual = resourceRegistry.getImage( IMAGE_DESCRIPTOR );

    assertThat( actual.getImageData().data ).isEqualTo( expectedImageData( IMAGE_DESCRIPTOR ) );
    assertThat( actual.isDisposed() ).isFalse();
  }

  @Test
  public void testGetImageTwice() {
    Image first = resourceRegistry.getImage( IMAGE_DESCRIPTOR );
    Image second = resourceRegistry.getImage( IMAGE_DESCRIPTOR );

    assertThat( first ).isSameAs( second );
  }

  @Test
  public void testGetImageWithNullAsImageDescriptor() {
    Image actual = resourceRegistry.getImage( null );

    assertThat( actual.getImageData().data ).isEqualTo( expectedImageData( MISSING_IMAGE_DESCRIPTOR ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testGetImageIfDisplayIsDisposed() {
    displayHelper.dispose();

    resourceRegistry.getImage( IMAGE_DESCRIPTOR );
  }

  @Test
  public void testDispose() {
    Color beforeColor = resourceRegistry.getColor( RGB_BLUE );
    Image beforeImage = resourceRegistry.getImage( IMAGE_DESCRIPTOR );
    resourceRegistry.dispose();
    Color afterColor = resourceRegistry.getColor( RGB_BLUE );
    Image afterImage = resourceRegistry.getImage( IMAGE_DESCRIPTOR );

    assertThat( beforeColor ).isNotSameAs( afterColor  );
    assertThat( beforeColor.isDisposed() ).isTrue();
    assertThat( afterColor .isDisposed() ).isFalse();
    assertThat( beforeImage ).isNotSameAs( afterImage );
    assertThat( beforeImage.isDisposed() ).isTrue();
    assertThat( afterImage .isDisposed() ).isFalse();
  }

  @Test
  public void testDisplayDisposal() {
    Color color = resourceRegistry.getColor( RGB_BLUE );
    Image image = resourceRegistry.getImage( IMAGE_DESCRIPTOR );

    displayHelper.dispose();

    assertThat( color.isDisposed() ).isTrue();
    assertThat( image.isDisposed() ).isTrue();
  }

  @Test( expected = NullPointerException.class )
  public void testConstructWithNullAsDisplay() {
    new ResourceRegistry( null );
  }

  @Before
  public void setUp() {
    resourceRegistry = new ResourceRegistry( displayHelper.getDisplay() );
  }

  private byte[] expectedImageData( ImageDescriptor imageDescriptor ) {
    return imageDescriptor.createImage( displayHelper.getDisplay() ).getImageData().data;
  }
}