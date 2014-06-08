package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.gonsole.pdetest.GitConsoleRule.assertThat;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.pdetest.GitConsoleRule;
import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;

public class ConsoleCommandInputPDETest {

  @Rule public final GitConsoleRule console = new GitConsoleRule();
  @Rule public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();

  @Test
  public void testEnterSimpleGitCommand() throws GitAPIException {
    console.open( repositories.create( "repo-1" ) );

    console.enterCommand( "status" );

    assertThat( console )
      .waitForChange()
      .containsLines( "status", "# On branch master" );
  }

  @Test
  public void testEnterChangeRepositoryCommand() throws GitAPIException {
    console.open( repositories.create( "repo-1", "repo-2" ) );

    console.enterCommand( "cr repo-2" );

    assertThat( console )
      .waitForChange()
      .containsLines( "cr repo-2", "Current repository is: repo-2" );
  }
}