package com.codeaffine.console.core;

public interface ConsoleCommandInterpreter {
  boolean isRecognized( String... commandLine );
  String execute( String... commandLine );
}