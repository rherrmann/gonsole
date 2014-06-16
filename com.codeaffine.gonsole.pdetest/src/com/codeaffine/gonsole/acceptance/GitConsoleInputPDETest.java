package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.assertThat;
import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.line;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.internal.InputObserver;
import com.codeaffine.gonsole.pdetest.GitConsoleBot;
import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;

public class GitConsoleInputPDETest {

  private static final int NEXT_LINE_OFFSET = 3;

  @Rule public final GitConsoleBot console = new GitConsoleBot();
  @Rule public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();

  @Test
  public void testEnterSimpleGitCommand() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "status" ), "# On branch master", line( "repo" ) );
  }

  @Test
  public void testEnterChangeRepositoryCommand() throws GitAPIException {
    console.open( repositories.create( "rep1", "rep2" ) );

    console.enterCommandLine( "cr rep2" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "rep1", "cr rep2" ), "Current repository is: rep2", line( "rep2" ) );
  }

  @Test
  public void testEnterGitCommandWithMultipleSpaces() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "log  -M" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "log  -M" ), line( "repo" ) );
  }

  @Test
  public void testEnterGitCommandWithGitPrefix() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "git status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "git status" ), "# On branch master" , line( "repo" ) );
  }

  @Test
  public void testEnterUnrecognizedCommand() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "foo" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "foo" ), "Unrecognized command: foo", line( "repo" ) );
  }

  @Test
  public void testEnterUnrecognizedCommandWithGitPrefix() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "git unknown" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "git unknown" ), "Unrecognized command: git", line( "repo" ) );
  }

  @Test
  public void testType() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.typeText( "abc" );

    assertThat( console )
      .containsLines( line( "repo", "abc" ) )
      .caretIsAtEnd();
  }

  @Test
  public void testTypeWithCaretBeforeEnd() throws GitAPIException {
    String repositoryName = "repo";
    int insertCaretPosition = ( repositoryName + InputObserver.PROMPT_POSTFIX ).length();
    int expectedCaretPosition = ( repositoryName + InputObserver.PROMPT_POSTFIX + "a" ).length();
    console.open( repositories.create( repositoryName ) );

    console.typeText( "bc" );
    console.positionCaret( insertCaretPosition );
    console.typeText( "a" );

    assertThat( console )
      .containsLines( line( repositoryName, "abc" ) )
      .caretIsAt( expectedCaretPosition );
  }

  @Test
  public void testTypeWithCaretBeforePrompt() throws GitAPIException {
    String repositoryName = "repo";
    int insertCaretPosition = repositoryName.length() / 2;
    console.open( repositories.create( repositoryName ) );

    console.typeText( "ab" );
    console.positionCaret( insertCaretPosition );
    console.typeText( "c" );

    assertThat( console )
      .containsLines( line( repositoryName, "abc" ) )
      .caretIsAtEnd();
  }

  @Test
  public void testTypeNewline() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo" ), line( "repo" ) );
  }

  @Test
  public void testEncoding() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "status äöü" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "status äöü" ),
                      "fatal: No argument is allowed: äöü",
                      line( "repo" ) );
  }

  @Test
  public void testPromptColor() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    assertThat( console ).hasPromptColorAt( 0 );
  }

  @Test
  public void testInputColor() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .hasInputColorAt( line( "repo", "status" ).length() );
  }

  @Test
  public void testOutputColor() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .hasOutputColorAt( line( "repo", "status" ).length() + NEXT_LINE_OFFSET );
  }

  @Test
  public void testErrorColor() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "foo" );

    assertThat( console )
      .hasProcessedCommandLine()
      .hasErrorColorAt( line( "repo", "foo" ).length() + NEXT_LINE_OFFSET );
  }
}