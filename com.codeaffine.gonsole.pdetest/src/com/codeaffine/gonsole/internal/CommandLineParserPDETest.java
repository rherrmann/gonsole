package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;

public class CommandLineParserPDETest {

  private CommandLineParser commandLineParser;

  @Test
  public  void testParseArgumentLessCommand() {
    CommandInfo commandInfo = commandLineParser.parse( "status" );

    assertThat( commandInfo.getCommand().getClass().getSimpleName() ).isEqualTo( "Status" );
    assertThat( commandInfo.getArguments() ).isEmpty();
  }

  @Test
  public  void testParse() {
    CommandInfo commandInfo = commandLineParser.parse( "commit", "-m" );

    assertThat( commandInfo.getCommand().getClass().getSimpleName() ).isEqualTo( "Commit" );
    assertThat( commandInfo.getArguments() ).containsOnly( "-m" );
  }

  @Test
  public void testParseUnknownCommand() {
    try {
      commandLineParser.parse( "unknown" );
      fail();
    } catch ( RuntimeException expected ) {
      assertThat( expected.getCause() ).isInstanceOf( CmdLineException.class );
    }
  }

  @Before
  public void setUp() {
    new PgmResourceBundle().initialize();
    commandLineParser = new CommandLineParser();
  }
}