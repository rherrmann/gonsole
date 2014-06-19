package com.codeaffine.gonsole.internal.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ColorScheme;
import com.codeaffine.test.util.swt.DisplayHelper;

public class ColorDefinitionTest {

  private static final RGB RGB_RED = new RGB( 255, 0, 0 );

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ColorDefinition colorDefinition;
  private ColorRegistry colorRegistry;
  private ColorScheme colorScheme;

  @Test
  public void testGetInputColor() {
    when( colorScheme.getInputColor() ).thenReturn( RGB_RED );

    Color actual = colorDefinition.getInputColor();

    assertThat( actual.getRGB() ).isEqualTo( RGB_RED );
  }

  @Test
  public void testGetOutputColor() {
    when( colorScheme.getOutputColor() ).thenReturn( RGB_RED );

    Color actual = colorDefinition.getOutputColor();

    assertThat( actual.getRGB() ).isEqualTo( RGB_RED );
  }

  @Test
  public void testGetPromptColor() {
    when( colorScheme.getPromptColor() ).thenReturn( RGB_RED );

    Color actual = colorDefinition.getPromptColor();

    assertThat( actual.getRGB() ).isEqualTo( RGB_RED );
  }

  @Test
  public void testGetErrorColor() {
    when( colorScheme.getErrorColor() ).thenReturn( RGB_RED );

    Color actual = colorDefinition.getErrorColor();

    assertThat( actual.getRGB() ).isEqualTo( RGB_RED );
  }

  @Test
  public void testDispose() {
    colorDefinition.dispose();

    verify( colorRegistry ).clear();
  }

  @Test( expected = IllegalStateException.class )
  public void testGetInputColorIfDisposed() {
    colorDefinition.dispose();

    colorDefinition.getInputColor();
  }

  @Test( expected = IllegalStateException.class )
  public void testGetOutputColorIfDisposed() {
    colorDefinition.dispose();

    colorDefinition.getOutputColor();
  }

  @Test( expected = IllegalStateException.class )
  public void testGetPromptColorIfDisposed() {
    colorDefinition.dispose();

    colorDefinition.getPromptColor();
  }

  @Test( expected = IllegalStateException.class )
  public void testGetErrorColorIfDisposed() {
    colorDefinition.dispose();

    colorDefinition.getErrorColor();
  }

  @Before
  public void setUp() {
    colorScheme = mock( ColorScheme.class );
    colorRegistry = stubColorRegistry( RGB_RED, SWT.COLOR_RED );
    colorDefinition = new ColorDefinition( colorScheme, colorRegistry );

  }

  private ColorRegistry stubColorRegistry( RGB rgb, int systemColorId ) {
    ColorRegistry result = mock( ColorRegistry.class );
    Color color = displayHelper.getDisplay().getSystemColor( systemColorId );
    when( result.getColor( rgb ) ).thenReturn( color );
    return result;
  }
}