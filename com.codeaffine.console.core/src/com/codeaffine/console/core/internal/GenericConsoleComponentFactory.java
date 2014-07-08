package com.codeaffine.console.core.internal;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.history.HistoryTracker;
import com.google.common.collect.ObjectArrays;

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
      result = ObjectArrays.concat( getHistoryTracker(), result );
    }
    return result;
  }

  @Override
  public ConsolePrompt createConsolePrompt( ConsoleOutput consoleOutput ) {
    return consoleComponentFactory.createConsolePrompt( consoleOutput );
  }

  @Override
  public ConsoleCommandInterpreter[] createCommandInterpreters( ConsoleOutput consoleOutput ) {
    ConsoleCommandInterpreter[] result = consoleComponentFactory.createCommandInterpreters( consoleOutput );
    if( getHistoryTracker() != null ) {
      result = ObjectArrays.concat( getHistoryTracker(), result );
    }
    return result;
  }
}