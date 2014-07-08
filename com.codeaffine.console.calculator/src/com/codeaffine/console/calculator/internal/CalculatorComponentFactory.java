package com.codeaffine.console.calculator.internal;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.history.HistoryTracker;

class CalculatorComponentFactory implements ConsoleComponentFactory {

  @Override
  public ContentProposalProvider[] createProposalProviders() {
    return new ContentProposalProvider[] { new CalculatorContentProposalProvider() };
  }

  @Override
  public ConsolePrompt createConsolePrompt( ConsoleOutput consoleOutput ) {
    return new CalculatorConsolePrompt( consoleOutput );
  }

  @Override
  public ConsoleCommandInterpreter[] createCommandInterpreters( ConsoleOutput consoleOutput ) {
    return new ConsoleCommandInterpreter[] { new CalculatorConsoleCommandInterpreter( consoleOutput ) };
  }

  @Override
  public HistoryTracker getHistoryTracker() {
    return null;
  }
}