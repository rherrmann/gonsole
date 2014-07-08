package com.codeaffine.console.core;

import com.codeaffine.console.core.history.HistoryTracker;


public interface ConsoleComponentFactory {
  ConsoleCommandInterpreter[] createCommandInterpreters( ConsoleOutput consoleOutput );
  ConsolePrompt createConsolePrompt( ConsoleOutput consoleOutput );
  ContentProposalProvider[] createProposalProviders();
  HistoryTracker getHistoryTracker();
}