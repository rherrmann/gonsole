package com.codeaffine.console.core.pdetest.bot;

import org.junit.runners.model.Statement;

class ConsoleBotStatement extends Statement {

  private final ConsoleBot gitConsoleRule;
  private final Statement base;

  ConsoleBotStatement( ConsoleBot gitConsoleRule , Statement base ) {
    this.gitConsoleRule = gitConsoleRule;
    this.base = base;
  }

  @Override
  public void evaluate() throws Throwable {
    try {
      base.evaluate();
    } finally {
      gitConsoleRule.cleanup();
    }
  }
}