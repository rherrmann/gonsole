package com.codeaffine.console.calculator.internal;

import com.codeaffine.console.core.ConsoleDefinition;
import com.codeaffine.console.core.ConsoleFactory;


public class CalculatorConsoleFactory extends ConsoleFactory {

  @Override
  protected ConsoleDefinition getConsoleDefinition() {
    return new CalculatorDefinition();
  }
}
