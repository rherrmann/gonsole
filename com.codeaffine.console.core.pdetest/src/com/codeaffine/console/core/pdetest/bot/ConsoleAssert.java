package com.codeaffine.console.core.pdetest.bot;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.toArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.google.common.base.Predicate;

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
    Assertions.assertThat( getVisiblePopups() ).isEmpty();
    return this;
  }

  public ConsoleAssert showsContentAssist() {
    Assertions.assertThat( getVisiblePopups() ).hasSize( 1 );
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
    while( getVisiblePopups().length < 2 ) {
      consoleBot.displayHelper.flushPendingEvents();
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

  private Shell[] getVisiblePopups() {
    Shell[] newShells = consoleBot.displayHelper.getNewShells();
    Iterable<Shell> visibleShells = filter( Arrays.asList( newShells ), new Predicate<Shell>() {
      @Override
      public boolean apply(Shell input) {
        return input.getVisible();
      }
    } );
    return toArray( visibleShells, Shell.class );
  }
}