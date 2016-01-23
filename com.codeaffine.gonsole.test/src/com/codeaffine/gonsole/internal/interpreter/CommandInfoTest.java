package com.codeaffine.gonsole.internal.interpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.eclipse.jgit.pgm.TextBuiltin;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.internal.interpreter.CommandInfo;

public class CommandInfoTest {

  private CommandInfo commandInfo;

  @Before
  public void setUp() {
    commandInfo = new CommandInfo();
  }

  @Test
  public void testInitialValues() {
    assertThat( commandInfo.getCommand() ).isNull();
    assertThat( commandInfo.getArguments() ).isEmpty();
    assertThat( commandInfo.isHelpRequested() ).isFalse();
  }

  @Test
  public void testGetCommand() {
    TextBuiltin expected = mock( TextBuiltin.class );

    commandInfo.command = expected;

    assertThat( commandInfo.getCommand() ).isSameAs( expected );
  }

  @Test
  public void testGetArguments() {
    String expected = "expected";

    commandInfo.arguments.add( expected );

    assertThat( commandInfo.getArguments() ).containsExactly( expected );
  }

  @Test
  public void testGetCommandName() {
    String expected = "expected";

    commandInfo.commandName = expected;

    assertThat( commandInfo.getCommandName() ).isEqualTo( expected );
  }

  @Test
  public void testIsHelpRequested() {
    boolean expected = true;

    commandInfo.helpRequested = expected;

    assertThat( commandInfo.isHelpRequested() ).isEqualTo( expected );
  }
}
