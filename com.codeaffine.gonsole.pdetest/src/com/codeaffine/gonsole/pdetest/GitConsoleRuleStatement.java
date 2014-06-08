package com.codeaffine.gonsole.pdetest;

import org.junit.runners.model.Statement;

class GitConsoleRuleStatement extends Statement {

  private final GitConsoleRule gitConsoleRule;
  private final Statement base;

  GitConsoleRuleStatement( GitConsoleRule gitConsoleRule , Statement base ) {
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