package com.codeaffine.gonsole.internal.gitconsole;

import java.io.File;

import com.codeaffine.gonsole.ConsoleOutput;
import com.codeaffine.gonsole.ConsolePrompt;
import com.codeaffine.gonsole.internal.gitconsole.interpreter.ControlCommandInterpreter;
import com.codeaffine.gonsole.internal.gitconsole.repository.CompositeRepositoryProvider;

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
    consoleOutput.write( repositoryName + Constants.PROMPT_POSTFIX );
  }
}