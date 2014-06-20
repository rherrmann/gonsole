package com.codeaffine.console.core.pdetest.console;

import com.codeaffine.console.core.ConsoleDefinition;
import com.codeaffine.console.core.ConsoleFactory;

public class TestConsoleFactory extends ConsoleFactory {

  @Override
  protected ConsoleDefinition getConsoleDefinition() {
    return new TestConsoleDefinition();
  }
}