package com.codeaffine.gonsole;

public interface ConsoleCommandInterpreter {
  boolean isRecognized( String... commandLine );
  String execute( String... commandLine );
}