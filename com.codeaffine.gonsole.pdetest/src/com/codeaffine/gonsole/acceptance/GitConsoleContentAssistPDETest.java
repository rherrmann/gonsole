package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.assertThat;
import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.line;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.swt.SWT;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.pdetest.GitConsoleBot;
import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;
import com.codeaffine.test.util.swt.DisplayHelper;


public class GitConsoleContentAssistPDETest {

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();
  @Rule public final GitConsoleBot console = new GitConsoleBot();
  @Rule public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();

  @Test
  public void testShowContentAssist() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withProposal( "add" );
  }

  @Test
  public void testShowContentAssistWithFilter() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsContentAssist().withProposal( "show" );
  }

  @Test
  public void testShowContentAssistWithFilterAndOngoingTyping() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );
    console.typeText( "t" );
    displayHelper.flushPendingEvents();

    assertThat( console ).showsContentAssist().withProposal( "status" );
  }

  @Test
  public void testShowContentAssistOnCompletedCommand() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "status " );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console ).showsNoContentAssist();
  }

  @Test
  public void testApplyContentProposal() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( "repo", "add" ) );
  }

  @Test
  public void testApplyFilteredContentProposal() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );
    console.selectFirstContentProposal();

    assertThat( console )
      .containsLines( line( "repo", "show" ) );
  }

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
}
