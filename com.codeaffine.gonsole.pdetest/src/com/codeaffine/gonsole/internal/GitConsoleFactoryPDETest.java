package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProviderFactory;

public class GitConsoleFactoryPDETest {

  private IConsoleManager consoleManager;
  private GitConsoleFactory consoleFactory;

  @Test
  public void testOpenConsole() {
    consoleFactory.openConsole();

    assertThat( consoleManager.getConsoles() ).hasSize( 1 );
    assertThat( consoleManager.getConsoles()[ 0 ] ).isInstanceOf( GitConsole.class );
  }

  @Test
  public void testOpenConsoleTwice() {
    consoleFactory.openConsole();

    consoleFactory.openConsole();

    assertThat( consoleManager.getConsoles() ).hasSize( 1 );
    assertThat( consoleManager.getConsoles()[ 0 ] ).isInstanceOf( GitConsole.class );
  }

  @Test
  public void testRepositoryProviderFactoryInvocation() {
    CompositeRepositoryProviderFactory repositoryProviderFactory = mock( CompositeRepositoryProviderFactory.class );
    GitConsoleFactory consoleFactory = new GitConsoleFactory( repositoryProviderFactory, consoleManager );

    consoleFactory.openConsole();

    verify( repositoryProviderFactory ).create();
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