package com.codeaffine.console.calculator.internal;

import com.codeaffine.console.core.ConsoleConfigurer;
import com.codeaffine.console.core.ConsoleFactory;


public class CalculatorConsoleFactory extends ConsoleFactory {

  @Override
  protected ConsoleConfigurer getConsoleConfigurer() {
    return new CalculatorConsoleConfigurer();
  }
}
