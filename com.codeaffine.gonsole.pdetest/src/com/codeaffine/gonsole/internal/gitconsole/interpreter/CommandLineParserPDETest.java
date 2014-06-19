package com.codeaffine.gonsole.internal.gitconsole.interpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;

public class CommandLineParserPDETest {

  private CommandLineParser commandLineParser;

  @Test
  public void testIsRecognized() {
    boolean actual = commandLineParser.isRecognized( "status" );

    assertThat( actual ).isTrue();
  }

  @Test
  public void testIsRecognizedWithUnknownArguments() {
    boolean actual = commandLineParser.isRecognized( "status", "--unknown" );

    assertThat( actual ).isTrue();
  }

  @Test
  public void testIsRecognizedWithUnknownCommand() {
    boolean actual = commandLineParser.isRecognized( "unknown" );

    assertThat( actual ).isFalse();
  }

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
  public  void testParseWithUnknownArguments() {
    CommandInfo commandInfo = commandLineParser.parse( "status", "foo" );

    assertThat( commandInfo.getCommand().getClass().getSimpleName() ).isEqualTo( "Status" );
    assertThat( commandInfo.getArguments() ).containsOnly( "foo" );
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