package com.codeaffine.console.calculator.internal;

import static com.codeaffine.console.core.ConsoleConstants.LINE_DELIMITER;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleOutput;

class CalculatorConsoleCommandInterpreter implements ConsoleCommandInterpreter {

  static final String SUM = "sum";

  private static final String SUM_RESULT = "The sum of %s and %s is %s" + LINE_DELIMITER;

  private final ConsoleOutput consoleOutput;

  CalculatorConsoleCommandInterpreter( ConsoleOutput consoleOutput ) {
    this.consoleOutput = consoleOutput;
  }

  @Override
  public boolean isRecognized( String... commandLine ) {
    return commandLine.length > 0 && SUM.equals( commandLine[ 0 ] );
  }

  @Override
  public String execute( String... commandLine ) {
    int sum = parseInt( commandLine[ 1 ] ) + parseInt( commandLine[ 2 ] );
    consoleOutput.write( String.format( SUM_RESULT, commandLine[ 1 ], commandLine[ 2 ], valueOf( sum ) ) );
    return null;
  }
}