package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.TextInputBot.offset;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter;
import com.codeaffine.console.core.pdetest.console.TestConsoleDefinition;

public class ContentAssistProcessorPDETest {

  private static final String COMMAND = TestConsoleCommandInterpreter.COMMANDS.iterator().next();

  @Rule public final ConsoleBot consoleBot = new ConsoleBot();

  private ContentAssistProcessor processor;
  private TextInputBot bot;

  @Test
  public void testComputeCompletionWithOffsetOnLineEnd() {
    ITextViewer viewer = bot.performTextInput( COMMAND );

    ICompletionProposal[] actual = processor.computeCompletionProposals( viewer, offset( COMMAND.length() ) );

    assertThat( actual ).hasSize( 1 );
  }

  @Test
  public void testComputeCompletionProposalsWithOffsetOnLineStart() {
    ITextViewer viewer = bot.performTextInput( COMMAND );

    ICompletionProposal[] actual = processor.computeCompletionProposals( viewer, offset( 0 ) );

    assertThat( actual ).hasSize( 2 );
  }

  @Test
  public void testComputeCompletionProposalsWithNonMatchingCommand() {
    ITextViewer viewer = bot.performTextInput( "unknown" );

    ICompletionProposal[] actual = processor.computeCompletionProposals( viewer, offset( "unknown".length() ) );

    assertThat( actual ).isEmpty();
  }

  @Before
  public void setUp() {
    processor = new ContentAssistProcessor( new TestConsoleDefinition().getConsoleComponentFactory() );
    bot = new TextInputBot( consoleBot );
  }
}