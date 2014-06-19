package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.OutputStream;

import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;


public class PromptWriter {

  public static final String PROMPT_POSTFIX = "> ";

  private final ConsoleOutput consoleOutput;
  private final CompositeRepositoryProvider repositoryProvider;

  public PromptWriter( ConsoleIOProvider consoleIOProvider,
                       CompositeRepositoryProvider repositoryProvider )
  {
    this( createConsoleOutput( consoleIOProvider ), repositoryProvider );
  }

  public PromptWriter( ConsoleOutput consoleOutput, CompositeRepositoryProvider repositoryProvider )
  {
    this.consoleOutput = consoleOutput;
    this.repositoryProvider = repositoryProvider;
  }

  public void write() {
    consoleOutput.write( getRepositoryName() + PromptWriter.PROMPT_POSTFIX );
  }

  private String getRepositoryName() {
    File repositoryLocation = repositoryProvider.getCurrentRepositoryLocation();
    return ControlCommandInterpreter.getRepositoryName( repositoryLocation );
  }

  private static ConsoleOutput createConsoleOutput( ConsoleIOProvider consoleIOProvider ) {
    OutputStream promptStream = consoleIOProvider.getPromptStream();
    return ConsoleOutput.create( promptStream, consoleIOProvider );
  }
}
