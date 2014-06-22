package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.offset;
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

  @Rule public final ConsoleBot console = new ConsoleBot();

  private ContentAssistProcessor processor;
  private ITextViewer viewer;

  @Test
  public void testComputeCompletionWithOffsetOnLineEnd() {
    console.typeText( COMMAND );

    ICompletionProposal[] actual = processor.computeCompletionProposals( viewer, offset( COMMAND.length() ) );

    assertThat( actual ).hasSize( 1 );
  }

  @Test
  public void testComputeCompletionProposalsWithOffsetOnLineStart() {
    console.typeText( COMMAND );

    ICompletionProposal[] actual = processor.computeCompletionProposals( viewer, offset( 0 ) );

    assertThat( actual ).hasSize( 2 );
  }

  @Test
  public void testComputeCompletionProposalsWithNonMatchingCommand() {
    console.typeText( "unknown" );

    ICompletionProposal[] actual = processor.computeCompletionProposals( viewer, offset( "unknown".length() ) );

    assertThat( actual ).isEmpty();
  }

  @Before
  public void setUp() {
    processor = new ContentAssistProcessor( new TestConsoleDefinition().getConsoleComponentFactory() );
    viewer = console.open( new TestConsoleDefinition() ).getPage().getViewer();
  }
}