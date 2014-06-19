package com.codeaffine.gonsole.internal.gitconsole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.internal.Console;

public class GitConsoleFactoryPDETest {

  private IConsoleManager consoleManager;
  private GitConsoleFactory consoleFactory;

  @Test
  public void testOpenConsole() {
    consoleFactory.openConsole();

    assertThat( consoleManager.getConsoles() ).hasSize( 1 );
    assertThat( consoleManager.getConsoles()[ 0 ] ).isInstanceOf( Console.class );
    assertThat( ( ( Console )consoleManager.getConsoles()[ 0 ] ).getImageDescriptor() ).isSameAs( new GitConsoleDefinition( Display.getCurrent() ).getImage() );
  }

  @Test
  public void testOpenConsoleTwice() {
    consoleFactory.openConsole();

    consoleFactory.openConsole();

    assertThat( consoleManager.getConsoles() ).hasSize( 1 );
    assertThat( consoleManager.getConsoles()[ 0 ] ).isInstanceOf( Console.class );
  }

  @Before
  public void setUp() {
    consoleManager = spy( ConsolePlugin.getDefault().getConsoleManager() );
    consoleFactory = new GitConsoleFactory();
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