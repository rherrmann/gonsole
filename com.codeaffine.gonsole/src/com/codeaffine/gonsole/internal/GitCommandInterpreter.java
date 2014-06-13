package com.codeaffine.gonsole.internal;

import java.io.File;

public class GitCommandInterpreter implements ConsoleCommandInterpreter {

  private final PgmResourceBundle pgmResourceBundleInitializer;
  private final CommandExecutor commandExecutor;
  private final CommandLineParser commandLineParser;

  public GitCommandInterpreter( ConsoleOutput consoleOutput, File gitDirectory ) {
    this( new PgmResourceBundle(),
          new CommandExecutor( consoleOutput, gitDirectory ),
          new CommandLineParser() );
  }

  GitCommandInterpreter( PgmResourceBundle pgmResourceBundleInitializer,
                  CommandExecutor commandExecutor,
                  CommandLineParser commandLineParser )
  {
    this.pgmResourceBundleInitializer = pgmResourceBundleInitializer;
    this.commandExecutor = commandExecutor;
    this.commandLineParser = commandLineParser;
  }

  @Override
  public boolean isRecognized( String... commandLine ) {
    pgmResourceBundleInitializer.initialize();
    return commandLineParser.isRecognized( commandLine );
  }

  @Override
  public String execute( String... commandLine ) {
    pgmResourceBundleInitializer.initialize();
    CommandInfo commandInfo = commandLineParser.parse( commandLine );
    return commandExecutor.execute( commandInfo );
  }
}