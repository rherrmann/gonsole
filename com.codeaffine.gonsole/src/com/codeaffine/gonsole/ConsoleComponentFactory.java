package com.codeaffine.gonsole;


public interface ConsoleComponentFactory {
  ConsoleCommandInterpreter[] createCommandInterpreters( ConsoleOutput consoleOutput );
  ConsolePrompt createConsolePrompt( ConsoleOutput consoleOutput );
}