package com.codeaffine.gonsole;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.internal.GenericConsole;
import com.codeaffine.gonsole.internal.GitConsoleConfigurer;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.test.util.workbench.ConsoleHelper;

public class GitConsoleFactoryPDETest {

  private ConsoleHelper consoleHelper;

  @Before
  public void setUp() {
    consoleHelper = new ConsoleHelper();
    consoleHelper.hideConsoleView();
  }

  @After
  public void tearDown() {
    consoleHelper.hideConsoleView();
  }

  @Test
  public void testOpenConsole() {
    new GitConsoleFactory().openConsole();

    assertThat( consoleHelper.getConsoles() ).hasSize( 1 );
  }

  @Test
  public void testOpenConsoleWithInitialRepositoryLocation() {
    File initialRepositoryLocation = new File( "repo" );

    new GitConsoleFactory( initialRepositoryLocation ).openConsole();

    GenericConsole console = ( GenericConsole )consoleHelper.getConsoles()[ 0 ];
    File currentRepositoryLocation = getCurrentRepositoryLocation( console );
    assertThat( currentRepositoryLocation ).isEqualTo( initialRepositoryLocation );
  }

  private static File getCurrentRepositoryLocation( GenericConsole console ) {
    GitConsoleConfigurer consoleConfigurer = ( GitConsoleConfigurer )console.getConsoleConfigurer();
    CompositeRepositoryProvider repositoryProvider = consoleConfigurer.getRepositoryProvider();
    return repositoryProvider.getCurrentRepositoryLocation();
  }

}