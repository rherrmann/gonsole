package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.gonsole.acceptance.GitConsolePrompts.line;

import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.gonsole.internal.Constants;
import com.codeaffine.gonsole.pdetest.DefaultLocaleRule;

public class GitConsoleInputPDETest {

  @Rule public final DefaultLocaleRule defaultLocaleRule = new DefaultLocaleRule( Locale.ENGLISH );
  @Rule public final ConsoleConfigurer configurer = new ConsoleConfigurer();
  @Rule public final ConsoleBot console = new ConsoleBot();

  @Test
  public void testEnterSimpleGitCommand() {
    console.open( configurer.create( "repo" ) );

    console.enterCommandLine( "status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "status" ), "# On branch master", line( "repo" ) );
  }

  @Test
  public void testEnterChangeRepositoryCommand() {
    console.open( configurer.create( "rep1", "rep2" ) );

    console.enterCommandLine( "use rep2" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "rep1", "use rep2" ), "Current repository is: rep2", line( "rep2" ) );
  }

  @Test
  public void testEnterGitCommandWithMultipleSpaces() {
    console.open( configurer.create( "repo" ) );

    console.enterCommandLine( "log  -M" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "log  -M" ), line( "repo" ) );
  }

  @Test
  public void testEnterGitCommandWithGitPrefix() {
    console.open( configurer.create( "repo" ) );

    console.enterCommandLine( "git status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "git status" ), "# On branch master" , line( "repo" ) );
  }

  @Test
  public void testEnterGitCommandWithIllegalArguments() {
    console.open( configurer.create( "repo" ) );

    console.enterCommandLine( "commit" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "commit" ),
                      "Option \"--message (-m)\" is required" ,
                      line( "repo" ) );
  }

  @Test
  public void testEnterUnrecognizedCommand() {
    console.open( configurer.create( "repo" ) );

    console.enterCommandLine( "foo" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "foo" ), "Unrecognized command: foo", line( "repo" ) );
  }

  @Test
  public void testEnterUnrecognizedCommandWithGitPrefix() {
    console.open( configurer.create( "repo" ) );

    console.enterCommandLine( "git foo" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "git foo" ), "Unrecognized command: foo", line( "repo" ) );
  }

  @Test
  public void testType() {
    console.open( configurer.create( "repo" ) );

    console.typeText( "abc" );

    assertThat( console )
      .containsLines( line( "repo", "abc" ) )
      .caretIsAtEnd();
  }

  @Test
  public void testTypeWithCaretBeforeEnd() {
    String repositoryName = "repo";
    int insertCaretPosition = ( repositoryName + Constants.PROMPT_POSTFIX ).length();
    int expectedCaretPosition = ( repositoryName + Constants.PROMPT_POSTFIX + "a" ).length();
    console.open( configurer.create( repositoryName ) );

    console.typeText( "bc" );
    console.positionCaret( insertCaretPosition );
    console.typeText( "a" );

    assertThat( console )
      .containsLines( line( repositoryName, "abc" ) )
      .caretIsAt( expectedCaretPosition );
  }

  @Test
  public void testTypeWithCaretBeforePrompt() {
    String repositoryName = "repo";
    int insertCaretPosition = repositoryName.length() / 2;
    console.open( configurer.create( repositoryName ) );

    console.typeText( "ab" );
    console.positionCaret( insertCaretPosition );
    console.typeText( "c" );

    assertThat( console )
      .containsLines( line( repositoryName, "abc" ) )
      .caretIsAtEnd();
  }

  @Test
  public void testTypeNewline() {
    console.open( configurer.create( "repo" ) );

    console.enterCommandLine( "" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo" ), line( "repo" ) );
  }

  @Test
  public void testEncoding() {
    console.open( configurer.create( "repo" ) );

    console.enterCommandLine( "status äöü" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "status äöü" ),
                      "No argument is allowed: äöü",
                      line( "repo" ) );
  }
}