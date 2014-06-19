package com.codeaffine.gonsole.internal.gitconsole;

import com.codeaffine.gonsole.ConsoleCommandInterpreter;
import com.codeaffine.gonsole.ConsoleComponentFactory;
import com.codeaffine.gonsole.ConsoleOutput;
import com.codeaffine.gonsole.ConsolePrompt;
import com.codeaffine.gonsole.internal.ControlCommandInterpreter;
import com.codeaffine.gonsole.internal.GitCommandInterpreter;
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
}