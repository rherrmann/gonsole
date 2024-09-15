package com.codeaffine.console.core.internal;

import java.io.InputStream;
import java.util.stream.Stream;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.history.HistoryTracker;

class GenericConsoleComponentFactory implements ConsoleComponentFactory {

  private final ConsoleComponentFactory consoleComponentFactory;

  GenericConsoleComponentFactory( ConsoleComponentFactory consoleComponentFactory ) {
    this.consoleComponentFactory = consoleComponentFactory;
  }

  @Override
  public HistoryTracker getHistoryTracker() {
    return consoleComponentFactory.getHistoryTracker();
  }

  @Override
  public ContentProposalProvider[] createProposalProviders() {
    ContentProposalProvider[] result = consoleComponentFactory.createProposalProviders();
    if( getHistoryTracker() != null ) {
      result = Stream.concat( Stream.of( getHistoryTracker() ), Stream.of( result ) ).toArray( ContentProposalProvider[]::new );
    }
    return result;
  }

  @Override
  public ConsolePrompt createConsolePrompt( ConsoleOutput consoleOutput ) {
    return consoleComponentFactory.createConsolePrompt( consoleOutput );
  }

  @Override
  public ConsoleCommandInterpreter[] createCommandInterpreters( ConsoleOutput stdOut, ConsoleOutput errorOut, InputStream stdIn ) {
    ConsoleCommandInterpreter[] result = consoleComponentFactory.createCommandInterpreters( stdOut, errorOut, stdIn );
    if( getHistoryTracker() != null ) {
      result = Stream.concat( Stream.of( getHistoryTracker() ), Stream.of( result ) ).toArray( ConsoleCommandInterpreter[]::new );
    }
    return result;
  }
}