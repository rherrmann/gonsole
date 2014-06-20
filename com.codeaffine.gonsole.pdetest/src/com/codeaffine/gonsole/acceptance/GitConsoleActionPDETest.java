package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.gonsole.acceptance.GitConsolePrompts.line;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.test.util.swt.DisplayHelper;

public class GitConsoleActionPDETest {

  @Rule public final ConsoleConfigurer configurer = new ConsoleConfigurer();
  @Rule public final ConsoleBot console = new ConsoleBot();

  @Test
  public void testClearAction() {
    console.open( configurer.create( "repo" ) );

    console.runToolBarAction( "Clear" );
    new DisplayHelper().flushPendingEvents();

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "" ) );
  }
}