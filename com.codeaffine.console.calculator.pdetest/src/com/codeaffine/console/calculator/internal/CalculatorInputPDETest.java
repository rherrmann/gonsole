package com.codeaffine.console.calculator.internal;


import static com.codeaffine.console.calculator.internal.CalculatorConsolePrompts.line;
import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;

public class CalculatorInputPDETest {

  @Rule public final ConsoleBot console = new ConsoleBot();

  @Test
  public void testEnterSumCommand() {
    console.open( new CalculatorDefinition() );

    console.enterCommandLine( "sum 1 1" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "sum 1 1" ), "The sum of 1 and 1 is 2", line() );
  }


  @Test
  public void testEnterUnknownCommand() {
    console.open( new CalculatorDefinition() );

    console.enterCommandLine( "unknown" );

    assertThat( console )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "unknown" ), "Unrecognized command: unknown", line() );
  }
}
