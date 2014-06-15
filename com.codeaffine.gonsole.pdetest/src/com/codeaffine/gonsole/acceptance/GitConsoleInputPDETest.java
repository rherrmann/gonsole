package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.assertThat;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.pdetest.GitConsoleBot;
import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;

public class GitConsoleInputPDETest {

  @Rule public final GitConsoleBot console = new GitConsoleBot();
  @Rule public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();

  @Test
  public void testEnterSimpleGitCommand() throws GitAPIException {
    console.open( repositories.create( "repo-1" ) );

    console.enterCommandLine( "status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( "repo-1>status", "# On branch master", "repo-1>" );
  }

  @Test
  public void testEnterChangeRepositoryCommand() throws GitAPIException {
    console.open( repositories.create( "repo-1", "repo-2" ) );

    console.enterCommandLine( "cr repo-2" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( "repo-1>cr repo-2", "Current repository is: repo-2", "repo-2>" );
  }

  @Test
  public void testEnterGitCommandWithMultipleSpaces() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "log  -M" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( "repo>log  -M", "repo>" );
  }

  @Test
  public void testEnterGitCommandWithGitPrefix() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "git status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( "repo>git status", "# On branch master", "repo>" );
  }

  @Test
  public void testEnterUnrecognizedCommand() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "foo" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( "repo>foo", "Unrecognized command: foo", "repo>" );
  }

  @Test
  public void testEnterUnrecognizedCommandWithGitPrefix() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "git unknown" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( "repo>git unknown", "Unrecognized command: git", "repo>" );
  }

  @Test
  public void testType() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "abc" );

    assertThat( console )
      .containsLines( "repo>abc" )
      .caretIsAtEnd();
  }

  @Test
  public void testTypeWithCaretBeforeEnd() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "bc" );
    console.positionCaret( 5 );
    console.typeText( "a" );

    assertThat( console )
      .containsLines( "repo>abc" )
      .caretIsAt( 6 );
  }

  @Test
  public void testTypeWithCaretBeforePrompt() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "ab" );
    console.positionCaret( 2 );
    console.typeText( "c" );

    assertThat( console )
      .containsLines( "repo>abc" )
      .caretIsAtEnd();
  }

  @Test
  public void testTypeNewline() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( "repo>", "repo>" );
  }

  @Test
  public void testEncoding() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "status äöü" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( "repo>status äöü", "fatal: No argument is allowed: äöü", "repo>" );
  }
}