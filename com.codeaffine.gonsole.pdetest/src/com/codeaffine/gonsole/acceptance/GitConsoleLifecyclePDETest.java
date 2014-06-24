package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.gonsole.acceptance.GitConsolePrompts.line;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;


public class GitConsoleLifecyclePDETest {

  @Rule public final ConsoleConfigurer configurer = new ConsoleConfigurer();
  @Rule public final ConsoleBot console = new ConsoleBot();

  @Test
  public void testReopenConsoleView() {
    console.open( configurer.create( "repo" ) );
    console.enterCommandLine( "status" );

    console.reopenConsoleView();

    assertThat( console )
      .caretIsAtEnd()
      .containsLines( line( "repo", "status" ), "# On branch master" , line( "repo" ) );
  }
}
