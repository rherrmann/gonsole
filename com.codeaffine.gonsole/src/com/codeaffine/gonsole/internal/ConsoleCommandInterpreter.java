package com.codeaffine.gonsole.internal;


public interface ConsoleCommandInterpreter {

  boolean isRecognized( String... commandLine );
  String execute( String... commandLine );
}