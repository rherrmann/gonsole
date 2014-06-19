package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.assertThat;
import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.line;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.pdetest.GitConsoleBot;
import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;
import com.codeaffine.test.util.swt.DisplayHelper;


public class GitConsoleActionPDETest {

  @Rule public final GitConsoleBot console = new GitConsoleBot();
  @Rule public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();

  @Test
  public void testClearAction() {
    console.open( repositories.create( "repo" ) );

    console.runToolBarAction( "Clear" );
    new DisplayHelper().flushPendingEvents();

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "" ) );
  }
}