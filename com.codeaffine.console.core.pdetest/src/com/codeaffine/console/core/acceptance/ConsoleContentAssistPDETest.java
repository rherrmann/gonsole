package com.codeaffine.console.core.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMAND_COMPLETE;
import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMAND_COMPLEX;
import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMAND_SIMPLE;
import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.PROMPT;
import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.line;

import org.eclipse.swt.SWT;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.console.core.pdetest.console.TestConsoleDefinition;
import com.codeaffine.test.util.ConditionalIgnoreRule;
import com.codeaffine.test.util.ConditionalIgnoreRule.ConditionalIgnore;
import com.codeaffine.test.util.GtkPlatform;
import com.codeaffine.test.util.swt.DisplayHelper;

public class ConsoleContentAssistPDETest {

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final ConsoleBot console = new ConsoleBot();

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testShowContentAssist() {
    console.open( new TestConsoleDefinition() );

    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withProposal( COMMAND_COMPLETE );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testShowContentAssistWithFilter() {
    console.open( new TestConsoleDefinition() );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withProposal( COMMAND_SIMPLE );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testShowContentAssistWithFilterAndOngoingTyping() {
    console.open( new TestConsoleDefinition() );

    console.typeText( "c" );
    console.typeKey( SWT.CTRL, ' ' );
    console.typeText( "omplex" );
    new DisplayHelper().flushPendingEvents();

    assertThat( console ).showsContentAssist().withProposal( COMMAND_COMPLEX );
  }

  @Test
  public void testShowContentAssistOnCompletedCommand() {
    console.open( new TestConsoleDefinition() );

    console.typeText( "complex " );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsNoContentAssist();
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testApplyContentProposal() {
    console.open( new TestConsoleDefinition() );

    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( COMMAND_COMPLETE ) );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testApplyFilteredContentProposal() {
    console.open( new TestConsoleDefinition() );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( COMMAND_SIMPLE ) );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testApplyFilteredContentProposalWithSelectedText() {
    console.open( new TestConsoleDefinition() );

    console.typeText( "sx" );
    console.selectText( PROMPT.length() + 1, 1 );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( COMMAND_SIMPLE ) );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testContentAssistHasImage() {
    console.open( new TestConsoleDefinition() );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withImage();
  }
}