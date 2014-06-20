package com.codeaffine.gonsole.internal.interpreter;

import org.eclipse.jgit.pgm.opt.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

class CommandLineParser {

  boolean isRecognized( String...commands ) {
    boolean result = false;
    try {
      new CmdLineParser( new CommandInfo() ).parseArgument( commands );
      result = true;
    } catch( CmdLineException notRecognized ) {
    }
    return result;
  }

  CommandInfo parse( String... commands ) {
    CommandInfo result = new CommandInfo();
    try {
      new CmdLineParser( result ).parseArgument( commands );
    } catch( CmdLineException e ) {
      throw new RuntimeException( e );
    }
    return result;
  }
}