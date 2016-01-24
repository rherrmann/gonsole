package com.codeaffine.console.core.pdetest.console;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.history.HistoryTracker;

public class TestConsoleComponentFactory implements ConsoleComponentFactory {

  @Override
  public ConsolePrompt createConsolePrompt( final ConsoleOutput consoleOutput ) {
    return new TestConsolePrompt( consoleOutput );
  }

  @Override
  public ConsoleCommandInterpreter[] createCommandInterpreters( final ConsoleOutput stdOut, ConsoleOutput errorOut ) {
    return new ConsoleCommandInterpreter[] { new TestConsoleCommandInterpreter( stdOut ) };
  }

  @Override
  public ContentProposalProvider[] createProposalProviders() {
    return new ContentProposalProvider[] { new TestContentProposalProvider() };
  }

  @Override
  public HistoryTracker getHistoryTracker() {
    return null;
  }
}