package com.codeaffine.gonsole.acceptance;

import static org.junit.Assert.assertEquals;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.TextConsolePage;

public class TextConsolePageAssert extends AbstractAssert<TextConsolePageAssert, TextConsolePage> {

  private TextConsolePageAssert( TextConsolePage actual ) {
    super( actual, TextConsolePageAssert.class );
  }

  public static TextConsolePageAssert assertThat( TextConsolePage actual ) {
    return new TextConsolePageAssert( actual );
  }

  public TextConsolePageAssert hasProcessedCommandLine() {
    new TextViewerChangeObserver( actual.getViewer() ).waitForChange();
    return this;
  }

  public TextConsolePageAssert caretIsAtEnd() {
    StyledText control = ( StyledText )actual.getControl();
    return caretIsAt( control.getCharCount() );
  }

  public TextConsolePageAssert caretIsAt( int caretOffset ) {
    StyledText control = ( StyledText )actual.getControl();
    Assertions.assertThat( control.getCaretOffset() ).isEqualTo( caretOffset );
    return this;
  }

  public TextConsolePageAssert containsLines( String ... lines ) {
    String expectedText = "";
    StyledText control = ( StyledText )actual.getControl();
    for( int i = 0; i < lines.length; i++ ) {
      expectedText += lines[ i ];
      if( i < lines.length - 1 ) {
        expectedText += control.getLineDelimiter();
      }
    }
    assertEquals( expectedText, control.getText() );
    return this;
  }
}