package com.codeaffine.gonsole.internal.gitconsole;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.gonsole.internal.gitconsole.contentassist.ControlCommandContentProposalProvider;
import com.codeaffine.gonsole.internal.gitconsole.contentassist.GitCommandContentProposalProvider;
import com.codeaffine.gonsole.internal.gitconsole.interpreter.ControlCommandInterpreter;
import com.codeaffine.gonsole.internal.gitconsole.interpreter.GitCommandInterpreter;
import com.codeaffine.gonsole.internal.gitconsole.repository.CompositeRepositoryProvider;

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