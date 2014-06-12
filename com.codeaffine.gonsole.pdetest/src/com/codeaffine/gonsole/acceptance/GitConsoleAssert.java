package com.codeaffine.gonsole.acceptance;

import static org.junit.Assert.assertEquals;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class GitConsoleAssert extends AbstractAssert<GitConsoleAssert, GitConsolePageBot> {

  private GitConsoleAssert( GitConsolePageBot actual ) {
    super( actual, GitConsoleAssert.class );
  }

  public static GitConsoleAssert assertThat( GitConsoleBot consoleRule ) {
    return new GitConsoleAssert( consoleRule.gitConsolePageBot );
  }

  public GitConsoleAssert hasProcessedCommandLine() {
    actual.waitForChange();
    return this;
  }

  public GitConsoleAssert caretIsAtEnd() {
    return caretIsAt( actual.getCharCount() );
  }

  public GitConsoleAssert caretIsAt( int caretOffset ) {
    Assertions.assertThat( actual.getCaretOffset() ).isEqualTo( caretOffset );
    return this;
  }

  public GitConsoleAssert containsLines( String... lines ) {
    String expectedText = "";
    for( int i = 0; i < lines.length; i++ ) {
      expectedText += lines[ i ];
      if( i < lines.length - 1 ) {
        expectedText += actual.getLineDelimiter();
      }
    }
    assertEquals( expectedText, actual.getText() );
    return this;
  }
}