package com.codeaffine.gonsole.internal;

import static com.codeaffine.gonsole.internal.GitConsoleConstants.ERROR_COLOR;
import static com.codeaffine.gonsole.internal.GitConsoleConstants.INPUT_COLOR;
import static com.codeaffine.gonsole.internal.GitConsoleConstants.OUTPUT_COLOR;
import static com.codeaffine.gonsole.internal.GitConsoleConstants.PROMPT_COLOR;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.graphics.RGB;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ColorScheme;
import com.codeaffine.test.util.swt.DisplayHelper;

public class GitConsoleColorSchemeTest {

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ColorScheme colorScheme;

  @Test
  public  void testGetInputColor() {
    RGB actual = colorScheme.getInputColor();

    assertThat( actual ).isEqualTo( getExpected( INPUT_COLOR ) );

  }

  @Test
  public  void testGetOutputColor() {
    RGB actual = colorScheme.getOutputColor();

    assertThat( actual ).isEqualTo( getExpected( OUTPUT_COLOR ) );
  }

  @Test
  public  void testGetPromptColor() {
    RGB actual = colorScheme.getPromptColor();

    assertThat( actual ).isEqualTo( getExpected( PROMPT_COLOR ) );
  }

  @Test
  public  void testGetErrorColor() {
    RGB actual = colorScheme.getErrorColor();

    assertThat( actual ).isEqualTo( getExpected( ERROR_COLOR ) );
  }

  @Before
  public void setUp() {
    colorScheme = new GitConsoleColorScheme( displayHelper.getDisplay() );
  }

  private RGB getExpected( int systemColorId ) {
    return displayHelper.getColorDefinition( systemColorId );
  }
}