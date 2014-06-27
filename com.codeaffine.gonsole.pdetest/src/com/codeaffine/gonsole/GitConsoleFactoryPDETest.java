package com.codeaffine.gonsole;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.internal.GenericConsole;
import com.codeaffine.gonsole.internal.GitConsoleConfigurer;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

public class GitConsoleFactoryPDETest {

  private IConsoleManager consoleManager;

  @Test
  public void testOpenConsole() {
    new GitConsoleFactory().openConsole();

    assertThat( consoleManager.getConsoles() ).hasSize( 1 );
  }

  @Test
  public void testOpenConsoleWithInitialRepositoryLocation() {
    File initialRepositoryLocation = new File( "repo" );

    new GitConsoleFactory( initialRepositoryLocation ).openConsole();

    GenericConsole console = ( GenericConsole )consoleManager.getConsoles()[ 0 ];
    File currentRepositoryLocation = getCurrentRepositoryLocation( console );
    assertThat( currentRepositoryLocation ).isEqualTo( initialRepositoryLocation );
  }

  @Before
  public void setUp() {
    consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    removeAllConsoles();
  }

  @After
  public void tearDown() {
    removeAllConsoles();
  }

  private static File getCurrentRepositoryLocation( GenericConsole console ) {
    GitConsoleConfigurer consoleConfigurer = ( GitConsoleConfigurer )console.getConsoleConfigurer();
    CompositeRepositoryProvider repositoryProvider = consoleConfigurer.getRepositoryProvider();
    return repositoryProvider.getCurrentRepositoryLocation();
  }

  private void removeAllConsoles() {
    consoleManager.removeConsoles( consoleManager.getConsoles() );
  }
}