package com.codeaffine.console.core.pdetest.console;

import com.codeaffine.console.core.ConsoleConfigurer;
import com.codeaffine.console.core.ConsoleFactory;

public class TestConsoleFactory extends ConsoleFactory {

  @Override
  protected ConsoleConfigurer getConsoleConfigurer() {
    return new TestConsoleConfigurer();
  }
}