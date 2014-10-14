package com.codeaffine.console.calculator.internal;


import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;

import org.eclipse.swt.SWT;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;

public class CalculatorContentAssistPDETest {

  @Rule public final ConsoleBot console = new ConsoleBot();

  @Test
  public void testShowContentAssistWithFilter() {
    console.open( new CalculatorConsoleConfigurer() );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withProposal( CalculatorConsoleCommandInterpreter.SUM );
  }
}