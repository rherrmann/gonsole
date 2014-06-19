package com.codeaffine.gonsole.internal.resource;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.test.util.swt.DisplayHelper;

public class ColorRegistryTest {

  private static final RGB RGB_BLUE = new RGB( 0, 0, 255 );

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ColorRegistry colorRegistry;

  @Test
  public void testGetColor() {
    Color actual = colorRegistry.getColor( RGB_BLUE );

    assertThat( actual.getRGB() ).isEqualTo( RGB_BLUE );
    assertThat( actual.isDisposed() ).isFalse();
  }

  @Test
  public void testGetColorTwice() {
    Color first = colorRegistry.getColor( RGB_BLUE );
    Color second = colorRegistry.getColor( RGB_BLUE );

    assertThat( first ).isSameAs( second );
  }

  @Test( expected = IllegalStateException.class )
  public void testGetColorIfDisplayIsDisposed() {
    displayHelper.dispose();

    colorRegistry.getColor( RGB_BLUE );
  }

  @Test
  public void testClear() {
    Color before = colorRegistry.getColor( RGB_BLUE );
    colorRegistry.clear();
    Color after= colorRegistry.getColor( RGB_BLUE );

    assertThat( before ).isNotSameAs( after );
    assertThat( before.isDisposed() ).isTrue();
    assertThat( after.isDisposed() ).isFalse();
  }

  @Test
  public void testDisplayDisposal() {
    Color actual = colorRegistry.getColor( RGB_BLUE );

    displayHelper.dispose();

    assertThat( actual.isDisposed() ).isTrue();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testConstructWithNullAsDisplay() {
    new ColorRegistry( null );
  }

  @Before
  public void setUp() {
    colorRegistry = new ColorRegistry( displayHelper.getDisplay() );
  }
}