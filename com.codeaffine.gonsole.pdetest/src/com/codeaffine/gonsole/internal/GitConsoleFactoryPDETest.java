package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GitConsoleFactoryPDETest {

  private IConsoleManager consoleManager;
  private GitConsoleFactory consoleFactory;

  @Test
  public void testOpenConsole() {
    consoleFactory.openConsole();

    assertThat( consoleManager.getConsoles() ).hasSize( 1 );
    assertThat( getImageDescriptor() ).isSameAs( new GitConsoleDefinition( Display.getCurrent() ).getImage() );
    assertThat( getTitle() ).isSameAs( new GitConsoleDefinition( Display.getCurrent() ).getTitle() );
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

  private ImageDescriptor getImageDescriptor() {
    return consoleManager.getConsoles()[ 0 ].getImageDescriptor();
  }

  private String getTitle() {
    return consoleManager.getConsoles()[ 0 ].getName();
  }
}