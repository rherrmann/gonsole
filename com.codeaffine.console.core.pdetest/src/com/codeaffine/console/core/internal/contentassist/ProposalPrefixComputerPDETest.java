package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.line;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.text.ITextViewer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.internal.Console;
import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.console.core.pdetest.console.TestConsoleDefinition;

public class ProposalPrefixComputerPDETest {

  private static final String TEXT = "stat";

  @Rule public final ConsoleBot consoleBot = new ConsoleBot();

  private ProposalPrefixComputer computer;
  private ITextViewer viewer;

  @Test
  public void testComputeWithOffsetAtLineEnd() {
    viewer = performTextInput( TEXT );

    String actual = computer.compute( viewer, offset( TEXT.length() ) );

    assertThat( actual ).isEqualTo( TEXT );
  }

  @Test
  public void testComputeWithOffsetAtCommandStart() {
    viewer = performTextInput( TEXT );

    String actual = computer.compute( viewer, offset( 0 ) );

    assertThat( actual ).isEqualTo( "" );
  }

  @Test
  public void testComputeWithOffsetInText() {
    viewer = performTextInput( TEXT );

    String actual = computer.compute( viewer, offset( 2 ) );

    assertThat( actual ).isEqualTo( TEXT.substring( 0, 2 ) );
  }

  @Test
  public void testComputeWithOffsetInBadLocation() {
    viewer = performTextInput( TEXT );

    String actual = computer.compute( viewer, offset( TEXT.length() + 1 ) );

    assertThat( actual ).isEqualTo( "" );
  }

  @Before
  public void setUp() {
    computer = new ProposalPrefixComputer();
  }

  private static int offset( int promptOffSet ) {
    return line( "" ).length() + promptOffSet;
  }

  private ITextViewer performTextInput( String text ) {
    Console console = consoleBot.open( new TestConsoleDefinition() );
    consoleBot.typeText( text );
    return console.getPage().getViewer();
  }
}