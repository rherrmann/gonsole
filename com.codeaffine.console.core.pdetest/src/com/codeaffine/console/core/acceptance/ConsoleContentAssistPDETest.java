package com.codeaffine.console.core.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMAND_COMPLETE;
import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMAND_COMPLEX;
import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMAND_SIMPLE;
import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.PROMPT;
import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.line;
import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.offset;
import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;

import org.eclipse.swt.SWT;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.console.core.pdetest.console.TestConsoleConfigurer;

public class ConsoleContentAssistPDETest {

  @Rule
  public final ConsoleBot console = new ConsoleBot();

  @Test
  public void testShowContentAssist() {
    console.open( new TestConsoleConfigurer() );

    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withProposal( COMMAND_COMPLETE );
  }

  @Test
  public void testShowContentAssistWithFilter() {
    console.open( new TestConsoleConfigurer() );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withProposal( COMMAND_SIMPLE );
  }

  @Test
  public void testShowContentAssistWithFilterAndOngoingTyping() {
    console.open( new TestConsoleConfigurer() );

    console.typeText( "c" );
    console.typeKey( SWT.CTRL, ' ' );
    console.typeText( "omplex" );
    flushPendingEvents();

    assertThat( console ).showsContentAssist().withProposal( COMMAND_COMPLEX );
  }

  @Test
  public void testShowContentAssistOnCompletedCommand() {
    console.open( new TestConsoleConfigurer() );

    console.typeText( "complex " );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsNoContentAssist();
  }

  @Test
  public void testApplyContentProposal() {
    console.open( new TestConsoleConfigurer() );

    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( COMMAND_COMPLETE + " " ) );
  }

  @Test
  public void testApplyFilteredContentProposal() {
    console.open( new TestConsoleConfigurer() );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( COMMAND_SIMPLE + " " ) );
  }

  @Test
  public void testApplyFilteredContentProposalWithSelectedText() {
    console.open( new TestConsoleConfigurer() );

    console.typeText( "sx" );
    console.selectText( PROMPT.length() + 1, 1 );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( COMMAND_SIMPLE + " ") );
  }

  @Test
  public void testApplyContentProposalWithTrailingText() {
    console.open( new TestConsoleConfigurer() );

    console.typeText( "s --arg" );
    console.positionCaret( offset( 1 ) );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .caretIsAt( offset( COMMAND_SIMPLE.length() ) )
      .containsLines( line( COMMAND_SIMPLE + " --arg" ) );
  }

  @Test
  public void testContentAssistHasImage() {
    console.open( new TestConsoleConfigurer() );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withImage();
  }
}