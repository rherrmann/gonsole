package com.codeaffine.console.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.internal.Console;
import com.codeaffine.console.core.pdetest.console.TestConsoleDefinition;
import com.codeaffine.console.core.pdetest.console.TestConsoleFactory;

public class ConsoleFactoryPDETest {

  private IConsoleManager consoleManager;
  private TestConsoleFactory consoleFactory;

  @Test
  public void testOpenConsole() {
    consoleFactory.openConsole();

    assertThat( consoleManager.getConsoles() ).hasSize( 1 );
    assertThat( consoleManager.getConsoles()[ 0 ] ).isInstanceOf( Console.class );
    assertThat( getImage() ).isSameAs( TestConsoleDefinition.IMAGE );
    assertThat( getTitle() ).isSameAs( new TestConsoleDefinition().getTitle() );
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

  private ImageDescriptor getImage() {
    return consoleManager.getConsoles()[ 0 ].getImageDescriptor();
  }

  private String getTitle() {
    return consoleManager.getConsoles()[ 0 ].getName();
  }
}