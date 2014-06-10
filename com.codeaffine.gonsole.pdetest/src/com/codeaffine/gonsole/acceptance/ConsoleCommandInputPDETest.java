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
    assertThat( console )
      .hasProcessedCommandLine();

    console.enterCommandLine( "status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( "repo-1>status", "# On branch master", "repo-1>" );
  }

  @Test
  public void testEnterChangeRepositoryCommand() throws GitAPIException {
    console.open( repositories.create( "repo-1", "repo-2" ) );
    assertThat( console )
      .hasProcessedCommandLine();

    console.enterCommandLine( "cr repo-2" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( "repo-1>cr repo-2", "Current repository is: repo-2", "repo-2>" );
  }
}