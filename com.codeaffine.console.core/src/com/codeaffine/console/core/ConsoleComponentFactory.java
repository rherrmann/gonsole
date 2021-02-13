package com.codeaffine.console.core;

import java.io.InputStream;

import com.codeaffine.console.core.history.HistoryTracker;


public interface ConsoleComponentFactory {
  ConsoleCommandInterpreter[] createCommandInterpreters( ConsoleOutput stdOut, ConsoleOutput errorOut, InputStream stdIn );
  ConsolePrompt createConsolePrompt( ConsoleOutput consoleOutput );
  ContentProposalProvider[] createProposalProviders();
  HistoryTracker getHistoryTracker();
}