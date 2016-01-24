package com.codeaffine.gonsole.internal.interpreter;

import static com.codeaffine.gonsole.internal.interpreter.ControlCommands.DESCRIPTION;

import java.io.StringWriter;
import java.util.MissingResourceException;

import org.eclipse.jgit.pgm.Command;
import org.eclipse.jgit.pgm.TextBuiltin;
import org.eclipse.jgit.pgm.opt.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

public class CommandLineParser {

  private final PgmResourceBundle pgmResourceBundle;

  public CommandLineParser() {
    pgmResourceBundle = new PgmResourceBundle();
  }

  public boolean isRecognized( String...commands ) {
    boolean result = false;
    try {
      new CmdLineParser( new CommandInfo() ).parseArgument( commands );
      result = true;
    } catch( CmdLineException notRecognized ) {
    }
    return result;
  }

  public CommandInfo parse( String... commands ) {
    CommandInfo result = new CommandInfo();
    result.commandName = commands.length > 0 ? commands[ 0 ] : "";
    result.helpRequested = TextBuiltin.containsHelp( commands );
    try {
      new CmdLineParser( result ).parseArgument( commands );
    } catch( CmdLineException e ) {
      throw new RuntimeException( e );
    }
    return result;
  }

  public String getDescription( String command ) {
    String result;
    if( DESCRIPTION.containsKey( command ) ) {
      result = DESCRIPTION.get( command );
    } else {
      result = getDescription( getCommandInfo( command ) );
    }
    return result;
  }

  public String getUsage( String command ) {
    String result;
    if( ControlCommands.USAGE.containsKey( command ) ) {
      result = ControlCommands.USAGE.get( command );
    } else {
      result = getUsage( getCommandInfo( command ) );
    }
    return result;
  }

  private String getUsage( CommandInfo commandInfo ) {
    StringWriter result = new StringWriter();
    String usage = getDescription( commandInfo );
    if( !usage.isEmpty() ) {
      result.append( usage );
      result.append( "\n" );
    }
    CmdLineParser parser = new CmdLineParser( commandInfo.getCommand() );
    parser.printSingleLineUsage( result, pgmResourceBundle.getResourceBundle() );
    if( result.getBuffer().length() > 0 ) {
      result.append( "\n\n" );
    }
    try {
      parser.printUsage( result, pgmResourceBundle.getResourceBundle() );
    } catch( MissingResourceException ignore ) {
      // [rh] workaround for Bug 457208: [pgm] resource bundle entry for "usage_bareClone" of the Clone command does not exist
      //      https://bugs.eclipse.org/bugs/show_bug.cgi?id=457208
    }
    return result.toString();
  }

  private String getDescription( CommandInfo commandInfo ) {
    String result = "";
    TextBuiltin command = commandInfo.getCommand();
    Command commandAnnotation = command == null ? null : command.getClass().getAnnotation( Command.class );
    if( commandAnnotation != null && !isNullOrEmpty( commandAnnotation.usage() ) ) {
      result = pgmResourceBundle.getResourceBundle().getString( commandAnnotation.usage() );
    }
    return result;
  }

  private static CommandInfo getCommandInfo( String command ) {
    CommandInfo result = new CommandInfo();
    CmdLineParser parser = new CmdLineParser( result );
    try {
      parser.parseArgument( command );
    } catch( Exception ignore ) {
    }
    return result;
  }

  private static boolean isNullOrEmpty( String string ) {
    return string == null || string.isEmpty();
  }
}