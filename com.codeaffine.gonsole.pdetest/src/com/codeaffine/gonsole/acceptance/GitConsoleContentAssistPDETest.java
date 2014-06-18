package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.assertThat;
import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.line;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.pdetest.GitConsoleBot;
import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;
import com.codeaffine.test.util.ConditionalIgnoreRule;
import com.codeaffine.test.util.ConditionalIgnoreRule.ConditionalIgnore;
import com.codeaffine.test.util.GtkPlatform;
import com.codeaffine.test.util.swt.DisplayHelper;


public class GitConsoleContentAssistPDETest {

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final GitConsoleBot console = new GitConsoleBot();
  @Rule public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testShowContentAssist() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withProposal( "add" );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testShowContentAssistWithFilter() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withProposal( "show" );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testShowContentAssistWithFilterAndOngoingTyping() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );
    console.typeText( "t" );
    new DisplayHelper().flushPendingEvents();

    assertThat( console ).showsContentAssist().withProposal( "status" );
  }

  @Test
  public void testShowContentAssistOnCompletedCommand() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "status " );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsNoContentAssist();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testShowContentForControlCommand() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "use" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withProposal( "use" );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testApplyContentProposal() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( "repo", "add" ) );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testApplyFilteredContentProposal() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( "repo", "show" ) );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testApplyFilteredContentProposalWithSelectedText() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "sx" );
    console.selectText( 7, 1 );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( "repo", "show" ) );
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testContentAssistHasGitCommandImage() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "status" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withImage();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testContentAssistHasControlCommandImage() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "use" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withImage();
  }

  @ConditionalIgnore(condition=GtkPlatform.class)
  @Test
  public void testProposalImagesDifferForProposalTypes() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

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
