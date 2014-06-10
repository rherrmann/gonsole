package com.codeaffine.gonsole.pdetest;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.console.TextConsolePage;

public class TextConsolePageAssert extends AbstractAssert<TextConsolePageAssert, TextConsolePage> {

  private TextConsolePageAssert( TextConsolePage actual ) {
    super( actual, TextConsolePageAssert.class );
  }

  public static TextConsolePageAssert assertThat( TextConsolePage actual ) {
    return new TextConsolePageAssert( actual );
  }

  public TextConsolePageAssert hasProcessedCommandLine() {
    DocumentChangeObserver observer = new DocumentChangeObserver();
    getDocument().addDocumentListener( observer );
    try {
      waitForChange( observer );
    } finally {
      getDocument().removeDocumentListener( observer );
    }
    return this;
  }

  public TextConsolePageAssert caretIsAtEnd() {
    StyledText control = ( StyledText )actual.getControl();
    assertEquals( control.getCharCount(), control.getCaretOffset() );
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

  private void waitForChange( DocumentChangeObserver observer ) {
    long startTime = System.currentTimeMillis();
    while( mustWait( observer ) ) {
      checkTimeout( startTime );
      sleep();
    }
  }

  private boolean mustWait( DocumentChangeObserver observer ) {
    return !observer.isChanged() && !getShell().isDisposed();
  }

  private static void checkTimeout( long startTime ) {
    if( System.currentTimeMillis() - startTime > 10000 ) {
      fail( "Timeout while waiting for the result of command line processing." );
    }
  }

  private IDocument getDocument() {
    return actual.getViewer().getDocument();
  }

  private void sleep() {
    if( !getDisplay().readAndDispatch() ) {
      getDisplay().sleep();
    }
  }

  private Display getDisplay() {
    return getShell().getDisplay();
  }

  private Shell getShell() {
    return actual.getControl().getShell();
  }
}