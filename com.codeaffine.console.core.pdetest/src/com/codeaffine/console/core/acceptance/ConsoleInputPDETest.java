package com.codeaffine.console.core.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMAND_SIMPLE;
import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.console.core.pdetest.console.TestConsoleConfigurer;


public class ConsoleInputPDETest {

  @Rule
  public final ConsoleBot console = new ConsoleBot();

  @Test // https://github.com/rherrmann/gonsole/issues/54
  public void testEnterEmptyCommandTwice() {
    console.open( new TestConsoleConfigurer() );

    console.typeEnter();
    console.typeEnter();
    console.enterCommandLine( COMMAND_SIMPLE );
    flushPendingEvents();

    assertThat( console ).hasProcessedCommandLine();

  }

}
