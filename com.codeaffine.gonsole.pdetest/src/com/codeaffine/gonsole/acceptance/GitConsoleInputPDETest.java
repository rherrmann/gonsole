package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.gonsole.acceptance.GitConsolePrompts.line;
import static com.codeaffine.gonsole.internal.GitConsoleConstants.PROMPT_POSTFIX;

import java.io.IOException;
import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.internal.GenericConsole;
import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.gonsole.internal.GitConsoleConfigurer;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.pdetest.DefaultLocaleRule;

public class GitConsoleInputPDETest {

  @Rule public final DefaultLocaleRule defaultLocaleRule = new DefaultLocaleRule( Locale.ENGLISH );
  @Rule public final GitConsoleHelper configurer = new GitConsoleHelper();
  @Rule public final ConsoleBot console = new ConsoleBot();

  @Test
  public void testEnterSimpleGitCommand() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.enterCommandLine( "status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "status" ), "# On branch master", line( "repo" ) );
  }

  @Test
  public void testEnterUseRepositoryCommand() {
    console.open( configurer.createConfigurer( "rep1", "rep2" ) );

    console.enterCommandLine( "use rep2" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "rep1", "use rep2" ), "Current repository is: rep2", line( "rep2" ) );
  }

  @Test
  public void testEnterUseRepositoryCommandWithUnknownRepository() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.enterCommandLine( "use foo" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "use foo" ), "Unknown repository", line( "repo" ) );
  }

  @Test
  public void testEnterUseRepositoryCommandWithAbolutePath() throws IOException {
    GenericConsole genericConsole = console.open( configurer.createConfigurer( "rep1", "rep2" ) );
    CompositeRepositoryProvider repositoryProvider = getRepositoryProvider( genericConsole );
    String repositoryLocation = repositoryProvider.getRepositoryLocations()[ 1 ].getCanonicalPath();

    console.enterCommandLine( "use \"" + repositoryLocation  + "\"" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "rep1", "use \"" + repositoryLocation  + "\""  ),
                      "Current repository is: rep2",
                      line( "rep2" ) );
  }

  @Test
  public void testEnterGitCommandWithMultipleSpaces() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.enterCommandLine( "log  -M" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "log  -M" ), line( "repo" ) );
  }

  @Test
  public void testEnterGitCommandWithGitPrefix() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.enterCommandLine( "git status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "git status" ), "# On branch master" , line( "repo" ) );
  }

  @Test
  public void testEnterGitCommandWithIllegalArguments() {
    console.open( configurer.createConfigurer( "repo" ) );

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
    console.open( configurer.createConfigurer( "repo" ) );

    console.enterCommandLine( "foo" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "foo" ), "Unrecognized command: foo", line( "repo" ) );
  }

  @Test
  public void testEnterUnrecognizedCommandWithGitPrefix() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.enterCommandLine( "git foo" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "git foo" ), "Unrecognized command: foo", line( "repo" ) );
  }

  @Test
  public void testType() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeText( "abc" );

    assertThat( console )
      .containsLines( line( "repo", "abc" ) )
      .caretIsAtEnd();
  }

  @Test
  public void testCorrectEnteredCommand() {
    String repositoryName = "repo";
    console.open( configurer.createConfigurer( repositoryName ) );

    console.typeText( "commit msg" );
    console.positionCaret( ( repositoryName + PROMPT_POSTFIX + "commit" ).length() );
    console.typeText( " " );
    flushPendingEvents();
    console.typeText( "-" );
    flushPendingEvents();
    console.typeText( "m" );
    flushPendingEvents();

    assertThat( console )
      .containsLines( line( "repo", "commit -m msg" ) );
  }

  @Test
  public void testTypeWithCaretBeforeEnd() {
    String repositoryName = "repo";
    int insertCaretPosition = ( repositoryName + PROMPT_POSTFIX ).length();
    int expectedCaretPosition = ( repositoryName + PROMPT_POSTFIX + "a" ).length();
    console.open( configurer.createConfigurer( repositoryName ) );

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
    console.open( configurer.createConfigurer( repositoryName ) );

    console.typeText( "ab" );
    console.positionCaret( insertCaretPosition );
    console.typeText( "c" );

    assertThat( console )
      .containsLines( line( repositoryName, "abc" ) )
      .caretIsAtEnd();
  }

  @Test
  public void testTypeNewline() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.enterCommandLine( "" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo" ), line( "repo" ) );
  }

  @Test
  public void testEncoding() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.enterCommandLine( "status äöü" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "status äöü" ),
                      "No argument is allowed: äöü",
                      line( "repo" ) );
  }

  @Test
  public void testPromptWithoutCurrentRepository() {
    console.open( configurer.createConfigurer( new String[ 0 ] ) );

    assertThat( console )
      .caretIsAtEnd()
      .containsLines( line( "no repository" ) );
  }

  private static CompositeRepositoryProvider getRepositoryProvider( GenericConsole console ) {
    GitConsoleConfigurer consoleConfigurer = ( GitConsoleConfigurer )console.getConsoleConfigurer();
    return consoleConfigurer.getRepositoryProvider();
  }
}