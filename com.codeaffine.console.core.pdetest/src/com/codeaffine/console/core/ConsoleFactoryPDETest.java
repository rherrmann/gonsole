package com.codeaffine.console.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.internal.GenericConsole;
import com.codeaffine.console.core.pdetest.console.TestConsoleFactory;

public class ConsoleFactoryPDETest {

  private IConsoleManager consoleManager;
  private TestConsoleFactory consoleFactory;

  @Test
  public void testOpenConsole() {
    consoleFactory.openConsole();

    assertThat( consoleManager.getConsoles() ).hasSize( 1 );
    assertThat( consoleManager.getConsoles()[ 0 ] ).isInstanceOf( GenericConsole.class );
  }

  @Test
  public void testOpenConsoleTwice() {
    consoleFactory.openConsole();

    consoleFactory.openConsole();

    IConsole[] consoles = consoleManager.getConsoles();
    assertThat( consoles ).hasSize( 2 );
    assertThat( consoles[ 0 ] ).isInstanceOf( GenericConsole.class );
    assertThat( consoles[ 1 ] ).isInstanceOf( GenericConsole.class );
    assertThat( consoles[ 0 ] ).isNotSameAs( consoles[ 1 ] );
  }

  @Before
  public void setUp() {
    consoleManager = spy( ConsolePlugin.getDefault().getConsoleManager() );
    consoleFactory = new TestConsoleFactory();
    removeAllConsoles();
  }

  @After
  public void tearDown() {
    removeAllConsoles();
  }

  private void removeAllConsoles() {
    consoleManager.removeConsoles( consoleManager.getConsoles() );
  }
}