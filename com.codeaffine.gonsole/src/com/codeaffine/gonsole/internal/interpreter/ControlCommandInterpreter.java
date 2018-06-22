package com.codeaffine.gonsole.internal.interpreter;

import static com.codeaffine.gonsole.internal.interpreter.ControlCommands.COMMANDS;
import static com.codeaffine.gonsole.internal.interpreter.ControlCommands.HELP;
import static com.codeaffine.gonsole.internal.interpreter.ControlCommands.USE;

import java.io.File;
import java.util.stream.Stream;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.history.HistoryTracker;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.util.Repositories;


public class ControlCommandInterpreter implements ConsoleCommandInterpreter {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final ConsoleOutput standardOutput;
  private final ConsoleOutput errorOutput;
  private final CompositeRepositoryProvider repositoryProvider;

  public ControlCommandInterpreter( ConsoleComponentFactory consoleComponentFactory,
                                    ConsoleOutput standardOutput,
                                    ConsoleOutput errorOutput,
                                    CompositeRepositoryProvider repositoryProvider )
  {
    this.consoleComponentFactory = consoleComponentFactory;
    this.standardOutput = standardOutput;
    this.errorOutput = errorOutput;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public boolean isRecognized( String... commandLine ) {
    return commandLine.length > 0 && COMMANDS.contains( commandLine[ 0 ] );
  }

  @Override
  public String execute( String... commandLine ) {
    String result = "";
    if( USE.equals( commandLine[ 0 ] ) ) {
      result = executeUse( commandLine );
    } else if( HELP.equals( commandLine[ 0 ] ) ) {
      executeHelp( commandLine );
    }
    return result;
  }

  private String executeUse( String... commandLine ) {
    String result;
    result = "Unknown repository";
    if( commandLine.length == 2 ) {
      String newRepository = commandLine[ 1 ];
      File repositoryLocation = findRepositoryLocationByName( newRepository );
      if( repositoryLocation == null ) {
        repositoryLocation = findRepositoryLocationByPath( newRepository );
      }
      if( repositoryLocation != null ) {
        result = null;
        repositoryProvider.setCurrentRepositoryLocation( repositoryLocation );
        String changedRepositoryName = Repositories.getRepositoryName( repositoryLocation );
        Object name = changedRepositoryName == null ? "unknown" : changedRepositoryName;
        String message = String.format( "Current repository is: %s", name );
        standardOutput.writeLine( message );
      }
    }
    return result;
  }

  private void executeHelp( String... commandLine ) {
    CommandLineParser commandLineParser = new CommandLineParser();
    if( commandLine.length == 2 ) {
      String helpTopic = commandLine[ 1 ];
      String usage = commandLineParser.getUsage( helpTopic );
      if( usage.isEmpty() ) {
        errorOutput.writeLine( "Unrecognized command: " + helpTopic );
      } else {
        errorOutput.write( usage );
      }
    } else {
      errorOutput.writeLine( "Press Ctrl+Space for content assist" );
      errorOutput.writeLine( "Press Up for command history" );
      errorOutput.writeLine( "" );
      errorOutput.writeLine( "Available Commands" );
      errorOutput.writeLine( "------------------" );
      String[] commands = getRecognizedCommands();
      int maxCommandLength = getMaxCommandLength();
      for( String command : commands ) {
        errorOutput.writeLine( padRight( command, maxCommandLength + 2 ) + commandLineParser.getDescription( command ) );
      }
    }
  }

  private String[] getRecognizedCommands() {
    return Stream.of( consoleComponentFactory.createProposalProviders() )
      .filter( proposalProvider -> !( proposalProvider instanceof HistoryTracker ) )
      .flatMap( proposalProvider -> Stream.of( proposalProvider.getContentProposals() ) )
      .map( Proposal::getText )
      .sorted()
      .toArray( String[]::new );
  }

  private int getMaxCommandLength() {
    return Stream.of( getRecognizedCommands() )
      .sorted( ( string1, string2 ) -> string1.length() > string2.length() ? -1 : 1 )
      .findFirst()
      .orElse( "" )
      .length();
  }

  private File findRepositoryLocationByName( String newRepositoryName ) {
    File result = null;
    for( File repositoryLocation : repositoryProvider.getRepositoryLocations() ) {
      if( newRepositoryName.equals( Repositories.getRepositoryName( repositoryLocation ) ) ) {
        result = repositoryLocation;
      }
    }
    return result;
  }

  private static File findRepositoryLocationByPath( String newRepository ) {
    File result = null;
    File path = new File( newRepository );
    if( path.getName().equals( ".git" ) && path.isDirectory() ) {
      result = path;
    }
    return result;
  }

  private static String padRight( String s, int n ) {
    return String.format( "%1$-" + n + "s", s );
  }

}
