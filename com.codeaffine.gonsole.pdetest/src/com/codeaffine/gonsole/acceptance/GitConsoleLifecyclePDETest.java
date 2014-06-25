package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.gonsole.acceptance.GitConsolePrompts.line;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;


public class GitConsoleLifecyclePDETest {

  @Rule public final ConsoleHelper configurer = new ConsoleHelper();
  @Rule public final ConsoleBot consoleBot = new ConsoleBot();

  @Test
  public void testReopenConsoleView() {
    consoleBot.open( configurer.createConfigurer( "repo" ) );
    consoleBot.enterCommandLine( "status" );

    consoleBot.reopenConsoleView();

    assertThat( consoleBot )
      .caretIsAtEnd()
      .containsLines( line( "repo", "status" ), "# On branch master" , line( "repo" ) );
  }
}
