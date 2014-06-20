package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.gonsole.acceptance.GitConsolePrompts.line;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.test.util.ConditionalIgnoreRule;
import com.codeaffine.test.util.ConditionalIgnoreRule.ConditionalIgnore;
import com.codeaffine.test.util.GtkPlatform;
import com.codeaffine.test.util.swt.DisplayHelper;

public class GitConsoleContentAssistPDETest {

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final ConsoleBot console = new ConsoleBot();
  @Rule public final ConsoleConfigurer configurer = new ConsoleConfigurer();

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testShowContentAssist() {
    console.open( configurer.create( "repo" ) );

    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withProposal( "add" );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testShowContentAssistWithFilter() {
    console.open( configurer.create( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withProposal( "show" );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testShowContentAssistWithFilterAndOngoingTyping() {
    console.open( configurer.create( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );
    console.typeText( "t" );
    new DisplayHelper().flushPendingEvents();

    assertThat( console ).showsContentAssist().withProposal( "status" );
  }

  @Test
  public void testShowContentAssistOnCompletedCommand() {
    console.open( configurer.create( "repo" ) );

    console.typeText( "status " );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsNoContentAssist();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testShowContentForControlCommand() {
    console.open( configurer.create( "repo" ) );

    console.typeText( "use" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withProposal( "use" );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testApplyContentProposal() {
    console.open( configurer.create( "repo" ) );

    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( "repo", "add" ) );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testApplyFilteredContentProposal() {
    console.open( configurer.create( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( "repo", "show" ) );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testApplyFilteredContentProposalWithSelectedText() {
    console.open( configurer.create( "repo" ) );

    console.typeText( "sx" );
    console.selectText( 7, 1 );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( "repo", "show" ) );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testContentAssistHasGitCommandImage() {
    console.open( configurer.create( "repo" ) );

    console.typeText( "status" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withImage();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testContentAssistHasControlCommandImage() {
    console.open( configurer.create( "repo" ) );

    console.typeText( "use" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withImage();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testProposalImagesDifferForProposalTypes() {
    console.open( configurer.create( "repo" ) );

    console.typeText( "status" );
    console.typeKey( SWT.CTRL, ' ' );
    Image gitCommandImage = console.getContentProposalImage( 0 );
    console.typeEnter();

    console.typeText( "use" );
    console.typeKey( SWT.CTRL, ' ' );
    Image controlCommandImage = console.getContentProposalImage( 0 );

    assertThat( gitCommandImage ).isNotSameAs( controlCommandImage );
  }
}