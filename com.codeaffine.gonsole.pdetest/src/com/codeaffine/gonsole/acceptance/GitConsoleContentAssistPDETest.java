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
  @Rule public final GitConsoleHelper configurer = new GitConsoleHelper();

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testShowContentAssist() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withProposal( "add" );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testShowContentAssistWithFilter() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withProposal( "show" );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testShowContentAssistWithFilterAndOngoingTyping() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );
    console.typeText( "t" );
    new DisplayHelper().flushPendingEvents();

    assertThat( console )
      .showsContentAssist()
      .withProposal( "status" );
  }

  @Test
  public void testShowContentAssistOnCompletedCommand() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeText( "status " );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsNoContentAssist();
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testShowContentForControlCommand() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeText( "use" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withProposal( "use" );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testApplyContentProposal() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( "repo", "add" ) );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testApplyFilteredContentProposal() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( "repo", "show" ) );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testApplyFilteredContentProposalWithSelectedText() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeText( "sx" );
    console.selectText( 7, 1 );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( "repo", "show" ) );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testContentAssistHasGitCommandImage() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeText( "status" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withImage();
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testContentAssistHasControlCommandImage() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeText( "use" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withImage();
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testProposalImagesDifferForProposalTypes() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeText( "status" );
    console.typeKey( SWT.CTRL, ' ' );
    Image gitCommandImage = console.getContentProposalImage( 0 );
    console.typeEnter();

    console.typeText( "use" );
    console.typeKey( SWT.CTRL, ' ' );
    Image controlCommandImage = console.getContentProposalImage( 0 );

    assertThat( gitCommandImage ).isNotSameAs( controlCommandImage );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testAdditionalInfo() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsAdditionalInfo()
      .containsText( "filepattern", "help", "update" );
  }
}