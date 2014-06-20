package com.codeaffine.gonsole.internal;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.gonsole.internal.contentassist.ControlCommandContentProposalProvider;
import com.codeaffine.gonsole.internal.contentassist.GitCommandContentProposalProvider;
import com.codeaffine.gonsole.internal.interpreter.ControlCommandInterpreter;
import com.codeaffine.gonsole.internal.interpreter.GitCommandInterpreter;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

class GitConsoleComponentFactory implements ConsoleComponentFactory {

  private final CompositeRepositoryProvider repositoryProvider;

  GitConsoleComponentFactory( CompositeRepositoryProvider repositoryProvidero ) {
    repositoryProvider = repositoryProvidero;
  }

  @Override
  public ConsoleCommandInterpreter[] createCommandInterpreters( ConsoleOutput consoleOutput ) {
    return new ConsoleCommandInterpreter[]{
      new ControlCommandInterpreter( consoleOutput, repositoryProvider ),
      new GitCommandInterpreter( consoleOutput, repositoryProvider )
    };
  }

  @Override
  public ConsolePrompt createConsolePrompt( final ConsoleOutput consoleOutput ) {
    return new GitConsolePrompt( repositoryProvider, consoleOutput );
  }

  @Override
  public ContentProposalProvider[] createProposalProviders() {
    return new ContentProposalProvider[] {
      new GitCommandContentProposalProvider(),
      new ControlCommandContentProposalProvider()
    };
  }
}