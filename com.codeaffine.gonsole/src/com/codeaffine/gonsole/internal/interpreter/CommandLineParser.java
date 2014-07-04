package com.codeaffine.gonsole.internal.interpreter;

import java.io.StringWriter;

import org.eclipse.jgit.pgm.opt.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

public class CommandLineParser {

  private final PgmResourceBundle pgmResourceBundle;

  public CommandLineParser() {
    pgmResourceBundle = new PgmResourceBundle();
  }

  public boolean isRecognized( String...commands ) {
    pgmResourceBundle.initialize();
    boolean result = false;
    try {
      new CmdLineParser( new CommandInfo() ).parseArgument( commands );
      result = true;
    } catch( CmdLineException notRecognized ) {
    }
    return result;
  }

  public CommandInfo parse( String... commands ) {
    pgmResourceBundle.initialize();
    CommandInfo result = new CommandInfo();
    try {
      new CmdLineParser( result ).parseArgument( commands );
    } catch( CmdLineException e ) {
      throw new RuntimeException( e );
    }
    return result;
  }

  public String getUsage( String command ) {
    pgmResourceBundle.initialize();
    CommandInfo commandInfo = new CommandInfo();
    CmdLineParser parser = new CmdLineParser( commandInfo );
    try {
      parser.parseArgument( command );
    } catch( Exception ignore ) {
    }
    return getUsage( commandInfo );
  }

  private String getUsage( CommandInfo commandInfo ) {
    StringWriter result = new StringWriter();
    CmdLineParser parser = new CmdLineParser( commandInfo.getCommand() );
    parser.printSingleLineUsage( result, pgmResourceBundle.getResourceBundle() );
    if( result.getBuffer().length() > 0 ) {
      result.append( "\n\n" );
    }
    parser.printUsage( result, pgmResourceBundle.getResourceBundle() );
    return result.toString();
  }
}