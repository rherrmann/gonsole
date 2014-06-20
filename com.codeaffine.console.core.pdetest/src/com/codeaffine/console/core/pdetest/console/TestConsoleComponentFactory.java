package com.codeaffine.console.core.pdetest.console;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;
import com.codeaffine.console.core.ContentProposalProvider;

class TestConsoleComponentFactory implements ConsoleComponentFactory {

  @Override
  public ConsolePrompt createConsolePrompt( final ConsoleOutput consoleOutput ) {
    return new TestConsolePrompt( consoleOutput );
  }

  @Override
  public ConsoleCommandInterpreter[] createCommandInterpreters( final ConsoleOutput consoleOutput ) {
    return new ConsoleCommandInterpreter[] { new TestConsoleCommandInterpreter( consoleOutput ) };
  }

  @Override
  public ContentProposalProvider[] createProposalProviders() {
    return new ContentProposalProvider[] { new TestContentProposalProvider() };
  }
}