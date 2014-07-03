package com.codeaffine.gonsole.internal.interpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;

import com.codeaffine.gonsole.internal.interpreter.CommandInfo;
import com.codeaffine.gonsole.internal.interpreter.CommandLineParser;
import com.codeaffine.gonsole.internal.interpreter.PgmResourceBundle;

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

  @Test
  public void testGetUsage() {
    String usage = commandLineParser.getUsage( "add" );

    assertThat( usage ).contains( "filepattern", "help", "update" );
  }

  @Test
  public void testGetUsageWithUnknownCommand() {
    String usage = commandLineParser.getUsage( "unknown" );

    assertThat( usage ).isEmpty();
  }

  @Before
  public void setUp() {
    new PgmResourceBundle().reset();
    commandLineParser = new CommandLineParser();
  }
}