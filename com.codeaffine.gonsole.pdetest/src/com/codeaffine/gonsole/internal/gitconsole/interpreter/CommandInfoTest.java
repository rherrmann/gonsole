package com.codeaffine.gonsole.internal.gitconsole.interpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.eclipse.jgit.pgm.TextBuiltin;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.internal.gitconsole.interpreter.CommandInfo;

public class CommandInfoTest {

  private CommandInfo commandInfo;

  @Test
  public void testInitialValues() {
    assertThat( commandInfo.getCommand() ).isNull();
    assertThat( commandInfo.getArguments() ).isEmpty();
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

  @Before
  public void setUp() {
    commandInfo = new CommandInfo();
  }
}
