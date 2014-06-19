package com.codeaffine.gonsole.pdetest;

import static org.junit.Assert.assertEquals;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.codeaffine.gonsole.internal.gitconsole.Constants;
import com.codeaffine.test.util.swt.DisplayHelper;

public class GitConsoleAssert extends AbstractAssert<GitConsoleAssert, GitConsolePageBot> {

  private final DisplayHelper displayHelper;

  private GitConsoleAssert( GitConsolePageBot actual, DisplayHelper displayHelper ) {
    super( actual, GitConsoleAssert.class );
    this.displayHelper = displayHelper;
  }

  public static GitConsoleAssert assertThat( GitConsoleBot consoleBot ) {
    return new GitConsoleAssert( consoleBot.gitConsolePageBot, consoleBot.displayHelper );
  }

  public static String line( String promptPrefix, String... commands ) {
    String result = promptPrefix + Constants.PROMPT_POSTFIX;
    for( String command : commands ) {
      result += command;
    }
    return result;
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

  public GitConsoleAssert showsNoContentAssist() {
    Assertions.assertThat( displayHelper.getNewShells() ).isEmpty();
    return this;
  }

  public GitConsoleAssert showsContentAssist() {
    Assertions.assertThat( displayHelper.getNewShells() ).hasSize( 1 );
    return this;
  }

  public GitConsoleAssert withProposal( String proposal ) {
    Table table = getContentProposalTable();
    Assertions.assertThat( table.getItem( 0 ).getText() ).isEqualTo( proposal );
    return this;
  }

  public void withImage() {
    Table table = getContentProposalTable();
    Assertions.assertThat( table.getItem( 0 ).getImage() ).isNotNull();
  }

  public GitConsoleAssert hasPromptColorAt( int offset ) {
    hasColorAt( offset, Constants.PROMPT_COLOR );
    return this;
  }

  public GitConsoleAssert hasInputColorAt( int offset ) {
    hasColorAt( offset, Constants.INPUT_COLOR );
    return this;
  }

  public GitConsoleAssert hasOutputColorAt( int offset ) {
    hasColorAt( offset, Constants.OUTPUT_COLOR );
    return this;
  }

  public GitConsoleAssert hasErrorColorAt( int offset ) {
    hasColorAt( offset, Constants.ERROR_COLOR );
    return this;
  }

  private void hasColorAt( int offset, int colorIndex ) {
    Color expectedColor = Display.getCurrent().getSystemColor( colorIndex );
    Color actualColor = actual.getForegroundAt( offset );
    Assertions.assertThat( actualColor ).isEqualTo( expectedColor );
  }

  private Table getContentProposalTable() {
    Shell shell = displayHelper.getNewShells()[ 0 ];
    return ( Table )shell.getChildren()[ 0 ];
  }
}