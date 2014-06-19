package com.codeaffine.gonsole.internal.gitconsole.interpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class GitCommandInterpreterTest {

  private static final String COMMAND_RESULT = "command-result";
  private static final String[] CMD_LINE = new String[ 0 ];

  private CommandExecutor commandExecutor;
  private CommandLineParser cmdLineParser;
  private GitCommandInterpreter gitInterpreter;

  @Test
  public void testIsRecognized() {
    when( cmdLineParser.isRecognized( CMD_LINE ) ).thenReturn( true );

    boolean actual = gitInterpreter.isRecognized( CMD_LINE );

    assertThat( actual ).isTrue();
  }

  @Test
  public void testExecute() {
    CommandInfo commandInfo = mock( CommandInfo.class );
    when( cmdLineParser.parse( CMD_LINE ) ).thenReturn( commandInfo );

    String actual = gitInterpreter.execute( CMD_LINE );

    InOrder order = inOrder( cmdLineParser, commandExecutor );
    order.verify( cmdLineParser ).parse( CMD_LINE );
    order.verify( commandExecutor ).execute( commandInfo );
    assertThat( actual ).isEqualTo( COMMAND_RESULT );
  }

  @Before
  public void setUp() {
    PgmResourceBundle pgmResourceBundle = mock( PgmResourceBundle.class );
    commandExecutor = mock( CommandExecutor.class );
    when( commandExecutor.execute( any( CommandInfo.class ) ) ).thenReturn( COMMAND_RESULT );
    cmdLineParser = mock( CommandLineParser.class );
    gitInterpreter = new GitCommandInterpreter( pgmResourceBundle, commandExecutor, cmdLineParser );
  }
}