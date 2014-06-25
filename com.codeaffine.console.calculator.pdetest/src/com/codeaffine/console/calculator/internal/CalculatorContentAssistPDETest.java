package com.codeaffine.console.calculator.internal;


import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;

import org.eclipse.swt.SWT;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.test.util.ConditionalIgnoreRule;
import com.codeaffine.test.util.ConditionalIgnoreRule.ConditionalIgnore;
import com.codeaffine.test.util.GtkPlatform;

public class CalculatorContentAssistPDETest {

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final ConsoleBot console = new ConsoleBot();

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void testShowContentAssistWithFilter() {
    console.open( new CalculatorDefinition() );

    console.typeText( "s" );
    console.typeKey( SWT.CTRL, ' ' );

    assertThat( console )
      .showsContentAssist()
      .withProposal( CalculatorConsoleCommandInterpreter.SUM );
  }
}