package com.codeaffine.console.core.pdetest.console;

import static java.util.Arrays.asList;

import java.util.Collection;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleOutput;

public class TestConsoleCommandInterpreter implements ConsoleCommandInterpreter {

  public static final String COMMAND_SIMPLE = "simple";
  public static final String COMMAND_COMPLEX = "complex";
  public static final String COMMAND_COMPLETE = "complete";
  public static final String DONE = "done";
  public final static Collection<String> COMMANDS = asList(
    COMMAND_SIMPLE, COMMAND_COMPLEX, COMMAND_COMPLETE
  );

  private final ConsoleOutput consoleOutput;

  TestConsoleCommandInterpreter( ConsoleOutput consoleOutput ) {
    this.consoleOutput = consoleOutput;
  }

  @Override
  public boolean isRecognized( String... commandLine ) {
    return COMMANDS.contains( commandLine[ 0 ] );
  }

  @Override
  public String execute( String... commandLine ) {
    consoleOutput.write( DONE + commandLine[ 0 ] );
    return null;
  }
}