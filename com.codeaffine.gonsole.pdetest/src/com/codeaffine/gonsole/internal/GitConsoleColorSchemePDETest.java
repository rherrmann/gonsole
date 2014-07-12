package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ColorScheme;
import com.codeaffine.test.util.swt.DisplayHelper;

public class GitConsoleColorSchemePDETest {

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ColorScheme colorScheme;

  @Test
  public  void testGetInputColor() {
    RGB actual = colorScheme.getInputColor();

    assertThat( actual ).isEqualTo( getExpected( SWT.COLOR_BLUE ) );
  }

  @Test
  public  void testGetOutputColor() {
    RGB actual = colorScheme.getOutputColor();

    assertThat( actual ).isEqualTo( getExpected( SWT.COLOR_WIDGET_FOREGROUND ) );
  }

  @Test
  public  void testGetPromptColor() {
    RGB actual = colorScheme.getPromptColor();

    assertThat( actual ).isEqualTo( getExpected( SWT.COLOR_DARK_GRAY ) );
  }

  @Test
  public  void testGetErrorColor() {
    RGB actual = colorScheme.getErrorColor();

    assertThat( actual ).isEqualTo( getExpected( SWT.COLOR_RED ) );
  }

  @Before
  public void setUp() {
    colorScheme = new GitConsoleColorScheme();
  }

  private RGB getExpected( int systemColorId ) {
    return displayHelper.getColorDefinition( systemColorId );
  }
}