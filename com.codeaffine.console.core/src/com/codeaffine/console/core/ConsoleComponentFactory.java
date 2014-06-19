package com.codeaffine.console.core;


public interface ConsoleComponentFactory {
  ConsoleCommandInterpreter[] createCommandInterpreters( ConsoleOutput consoleOutput );
  ConsolePrompt createConsolePrompt( ConsoleOutput consoleOutput );
  ContentProposalProvider[] createProposalProviders();
}