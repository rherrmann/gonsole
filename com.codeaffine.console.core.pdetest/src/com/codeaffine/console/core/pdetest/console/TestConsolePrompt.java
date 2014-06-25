package com.codeaffine.console.core.pdetest.console;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;

public class TestConsolePrompt implements ConsolePrompt {

  public static final String PROMPT = "prompt$ ";

  private final ConsoleOutput consoleOutput;

  TestConsolePrompt( ConsoleOutput consoleOutput ) {
    this.consoleOutput = consoleOutput;
  }

  @Override
  public void writePrompt() {
    consoleOutput.write( PROMPT );
  }

  public static int offset( int promptOffset ) {
    return line( "" ).length() + promptOffset;
  }

  public static String line( String... commands ) {
    String result = PROMPT;
    for( String command : commands ) {
      result += command;
    }
    return result;
  }
}