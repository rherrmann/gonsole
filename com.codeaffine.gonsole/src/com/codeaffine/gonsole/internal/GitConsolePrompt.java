package com.codeaffine.gonsole.internal;

import static com.codeaffine.gonsole.internal.GitConsoleConstants.PROMPT_POSTFIX;

import java.io.File;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;
import com.codeaffine.gonsole.internal.interpreter.ControlCommandInterpreter;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

class GitConsolePrompt implements ConsolePrompt {

  private final CompositeRepositoryProvider repositoryProvider;
  private final ConsoleOutput consoleOutput;

  GitConsolePrompt( CompositeRepositoryProvider repositoryProvider, ConsoleOutput consoleOutput ) {
    this.repositoryProvider = repositoryProvider;
    this.consoleOutput = consoleOutput;
  }

  @Override
  public void writePrompt() {
    File gitDirectory = repositoryProvider.getCurrentRepositoryLocation();
    String repositoryName = ControlCommandInterpreter.getRepositoryName( gitDirectory );
    consoleOutput.write( repositoryName + PROMPT_POSTFIX );
  }
}