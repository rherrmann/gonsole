package com.codeaffine.gonsole.internal.interpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;

public class CommandLineParserPDETest {

  private CommandLineParser commandLineParser;

  @Before
  public void setUp() {
    commandLineParser = new CommandLineParser();
  }

  @Test
  public void testIsRecognized() {
    boolean actual = commandLineParser.isRecognized( "status" );

    assertThat( actual ).isTrue();
  }

  @Test
  public void testIsRecognizedWithUnknownOption() {
    boolean actual = commandLineParser.isRecognized( "status", "--unknown" );

    assertThat( actual ).isTrue();
  }

  @Test
  public void testIsRecognizedWithUnknownCommand() {
    boolean actual = commandLineParser.isRecognized( "unknown" );

    assertThat( actual ).isFalse();
  }

  @Test
  public  void testParseCommandWithoutOption() {
    CommandInfo commandInfo = commandLineParser.parse( "status" );

    assertThat( commandInfo.getCommand().getClass().getSimpleName() ).isEqualTo( "Status" );
    assertThat( commandInfo.getArguments() ).isEmpty();
    assertThat( commandInfo.isHelpRequested() ).isFalse();
  }

  @Test
  public  void testParse() {
    CommandInfo commandInfo = commandLineParser.parse( "commit", "-m" );

    assertThat( commandInfo.getCommand().getClass().getSimpleName() ).isEqualTo( "Commit" );
    assertThat( commandInfo.getCommandName() ).isEqualTo( "commit" );
    assertThat( commandInfo.getArguments() ).containsOnly( "-m" );
    assertThat( commandInfo.isHelpRequested() ).isFalse();
  }

  @Test
  public  void testParseWithHelpRequest() {
    CommandInfo commandInfo = commandLineParser.parse( "commit", "-h" );

    assertThat( commandInfo.getCommand().getClass().getSimpleName() ).isEqualTo( "Commit" );
    assertThat( commandInfo.getCommandName() ).isEqualTo( "commit" );
    assertThat( commandInfo.getArguments() ).isEmpty();
    assertThat( commandInfo.isHelpRequested() ).isTrue();
  }

  @Test
  public  void testParseWithLongHelpRequest() {
    CommandInfo commandInfo = commandLineParser.parse( "commit", "--help" );

    assertThat( commandInfo.getCommand().getClass().getSimpleName() ).isEqualTo( "Commit" );
    assertThat( commandInfo.getCommandName() ).isEqualTo( "commit" );
    assertThat( commandInfo.getArguments() ).isEmpty();
    assertThat( commandInfo.isHelpRequested() ).isTrue();
  }

  @Test
  public void testParseWithOptionAndHelpRequest() {
    CommandInfo commandInfo = commandLineParser.parse( "commit", "--help", "--amend" );

    assertThat( commandInfo.getCommand().getClass().getSimpleName() ).isEqualTo( "Commit" );
    assertThat( commandInfo.getCommandName() ).isEqualTo( "commit" );
    assertThat( commandInfo.getArguments() ).isEmpty();
    assertThat( commandInfo.isHelpRequested() ).isTrue();
  }

  @Test
  public  void testParseWithUnknownOption() {
    CommandInfo commandInfo = commandLineParser.parse( "status", "--foo" );

    assertThat( commandInfo.getCommand().getClass().getSimpleName() ).isEqualTo( "Status" );
    assertThat( commandInfo.getCommandName() ).isEqualTo( "status" );
    assertThat( commandInfo.getArguments() ).containsOnly( "--foo" );
    assertThat( commandInfo.isHelpRequested() ).isFalse();
  }

  @Test
  public void testParseKnownCommandInWrongCasing() {
    try {
      commandLineParser.parse( "CommIT" );
      fail();
    } catch ( RuntimeException expected ) {
      assertThat( expected.getCause() ).isInstanceOf( CmdLineException.class );
    }
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

    assertThat( usage ).contains( "Add file", "filepattern", "help", "update" );
  }

  @Test
  public void testGetUsageWithUnknownCommand() {
    String usage = commandLineParser.getUsage( "unknown" );

    assertThat( usage ).isEmpty();
  }
}