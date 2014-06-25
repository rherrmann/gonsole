package com.codeaffine.console.calculator.internal;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;

class CalculatorConsolePrompt implements ConsolePrompt {

  public static final String PROMPT = "prompt$ ";

  private final ConsoleOutput consoleOutput;

  CalculatorConsolePrompt( ConsoleOutput consoleOutput ) {
    this.consoleOutput = consoleOutput;
  }

  @Override
  public void writePrompt() {
    consoleOutput.write( PROMPT );
  }
}