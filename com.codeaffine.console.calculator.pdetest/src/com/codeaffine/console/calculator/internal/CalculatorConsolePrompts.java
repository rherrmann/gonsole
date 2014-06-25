package com.codeaffine.console.calculator.internal;

import static com.codeaffine.console.calculator.internal.CalculatorConsolePrompt.PROMPT;

class CalculatorConsolePrompts {

  public static int offset( int promptOffSet ) {
    return line( "" ).length() + promptOffSet;
  }

  public static String line( String... commands ) {
    String result = PROMPT;
    for( String command : commands ) {
      result += command;
    }
    return result;
  }
}