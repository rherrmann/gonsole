package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.gonsole.acceptance.GitConsolePrompts.line;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.internal.GenericConsole;
import com.codeaffine.console.core.pdetest.bot.ConsoleBot;

public class GitConsoleActionPDETest {

  @Rule public final GitConsoleHelper configurer = new GitConsoleHelper();
  @Rule public final ConsoleBot consoleBot = new ConsoleBot();

  @Test
  public void testClearAction() {
    consoleBot.open( configurer.createConfigurer( "repo" ) );
    consoleBot.typeText( "to be cleared" );

    consoleBot.runToolBarAction( "Clear" );

    assertThat( consoleBot )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo" ) );
  }

  @Test
  public void testCloseAction() {
    GenericConsole console = consoleBot.open( configurer.createConfigurer( "repo" ) );

    consoleBot.runToolBarAction( "Close" );

    assertThat( console.isDisposed() ).isTrue();
  }
}