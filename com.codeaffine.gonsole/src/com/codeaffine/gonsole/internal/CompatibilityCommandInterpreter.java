package com.codeaffine.gonsole.internal;

import static com.google.common.collect.Iterables.skip;
import static com.google.common.collect.Iterables.toArray;

import java.io.File;
import java.util.Arrays;


public class CompatibilityCommandInterpreter implements ConsoleCommandInterpreter {

  private final ConsoleCommandInterpreter gitCommandInterpreter;

  public CompatibilityCommandInterpreter( ConsoleOutput consoleOutput, File repositoryLocation ) {
    gitCommandInterpreter = new GitCommandInterpreter( consoleOutput, repositoryLocation );
  }

  @Override
  public boolean isRecognized( String... commandLine ) {
    boolean result = isGitPrefixed( commandLine );
    if( result ) {
      result = gitCommandInterpreter.isRecognized( subCommandLine( commandLine ) );
    }
    return result;
  }

  @Override
  public String execute( String... commandLine ) {
    String[] array = subCommandLine( commandLine );
    return gitCommandInterpreter.execute( array );
  }

  private static boolean isGitPrefixed( String... commandLine ) {
    return commandLine.length >= 2 && "git".equals( commandLine[ 0 ] );
  }

  private static String[] subCommandLine( String... commandLine ) {
    return toArray( skip( Arrays.asList( commandLine ), 1 ), String.class );
  }
}
