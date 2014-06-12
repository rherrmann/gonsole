package com.codeaffine.gonsole.acceptance;

import org.junit.runners.model.Statement;

class GitConsoleBotStatement extends Statement {

  private final GitConsoleBot gitConsoleRule;
  private final Statement base;

  GitConsoleBotStatement( GitConsoleBot gitConsoleRule , Statement base ) {
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