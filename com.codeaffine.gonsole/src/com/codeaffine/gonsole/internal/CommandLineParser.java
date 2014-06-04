package com.codeaffine.gonsole.internal;

import org.eclipse.jgit.pgm.opt.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

class CommandLineParser {

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