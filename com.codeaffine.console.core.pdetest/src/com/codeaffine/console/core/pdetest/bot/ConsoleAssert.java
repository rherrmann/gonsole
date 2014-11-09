package com.codeaffine.console.core.pdetest.bot;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Table;

public class ConsoleAssert extends AbstractAssert<ConsoleAssert, ConsolePageBot> {

  private final ConsoleBot consoleBot;

  private ConsoleAssert( ConsoleBot consoleBot ) {
    super( consoleBot.consolePageBot, ConsoleAssert.class );
    this.consoleBot = consoleBot;
  }

  public static ConsoleAssert assertThat( ConsoleBot consoleBot ) {
    return new ConsoleAssert( consoleBot );
  }

  public ConsoleAssert hasProcessedCommandLine() {
    actual.waitForCommandLineProcessing();
    return this;
  }

  public ConsoleAssert caretIsAtEnd() {
    return caretIsAt( actual.getCharCount() );
  }

  public ConsoleAssert caretIsAt( int caretOffset ) {
    Assertions.assertThat( actual.getCaretOffset() ).isEqualTo( caretOffset );
    return this;
  }

  public ConsoleAssert containsLines( String... lines ) {
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

  public ConsoleAssert showsNoContentAssist() {
    Assertions.assertThat( consoleBot.displayHelper.getNewShells() ).isEmpty();
    return this;
  }

  public ConsoleAssert showsContentAssist() {
    Assertions.assertThat( consoleBot.displayHelper.getNewShells() ).hasSize( 1 );
    return this;
  }

  public ConsoleAssert withProposal( String proposal ) {
    Table table = consoleBot.getContentProposalTable();
    Assertions.assertThat( table.getItem( 0 ).getText() ).isEqualTo( proposal );
    return this;
  }

  public void withImage() {
    Table table = consoleBot.getContentProposalTable();
    Assertions.assertThat( table.getItem( 0 ).getImage() ).isNotNull();
  }

  public ConsoleAssert showsAdditionalInfo() {
    long start = System.currentTimeMillis();
    while( consoleBot.displayHelper.getNewShells().length < 2 ) {
      flushPendingEvents();
      if( System.currentTimeMillis() - start > 5000 ) {
        fail( "Timed out while waiting for additional info shell" );
      }
    }
    return this;
  }

  public void containsText( String... texts ) {
    for( String text : texts ) {
      Assertions.assertThat( consoleBot.getAdditionalInfoText() ).contains( text );
    }
  }

  public ConsoleAssert hasPromptColorAt( int offset ) {
    hasColorAt( offset, consoleBot.console.getColorScheme().getPromptColor() );
    return this;
  }

  public ConsoleAssert hasInputColorAt( int offset ) {
    hasColorAt( offset, consoleBot.console.getColorScheme().getInputColor());
    return this;
  }

  public ConsoleAssert hasOutputColorAt( int offset ) {
    hasColorAt( offset, consoleBot.console.getColorScheme().getOutputColor() );
    return this;
  }

  public ConsoleAssert hasErrorColorAt( int offset ) {
    hasColorAt( offset, consoleBot.console.getColorScheme().getErrorColor() );
    return this;
  }

  private void hasColorAt( int offset, RGB rgb ) {
    actual.waitForColoring( offset, rgb );
  }
}