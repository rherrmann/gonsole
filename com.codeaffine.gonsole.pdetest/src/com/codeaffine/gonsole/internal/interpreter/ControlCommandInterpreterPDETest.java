package com.codeaffine.gonsole.internal.interpreter;

import static com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper.createWithSingleChildProvider;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.gonsole.acceptance.GitConsoleHelper;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

public class ControlCommandInterpreterPDETest {

  private static final String REPO_NAME = "repo";

  @Rule
  public final GitConsoleHelper consoleHelper = new GitConsoleHelper();

  private CompositeRepositoryProvider repositoryProvider;
  private File repositoryLocation;
  private ConsoleComponentFactory consoleComponentFactory;
  private ConsoleOutput errorOutput;
  private ControlCommandInterpreter interpreter;

  @Before
  public void setUp() throws IOException {
    repositoryLocation = createRepository( REPO_NAME );
    repositoryProvider = createWithSingleChildProvider( repositoryLocation );
    repositoryProvider.setCurrentRepositoryLocation( null );
    consoleComponentFactory = mock( ConsoleComponentFactory.class );
    when( consoleComponentFactory.createProposalProviders() ).thenReturn( new ContentProposalProvider[ 0 ] );
    errorOutput = mock( ConsoleOutput.class );
    interpreter = createControlCommandInterpreter();
  }

  @Test
  public void testUseWithAbolutePathToUnregisteredRepository() {
    interpreter.execute( "use", repositoryLocation.getAbsolutePath() );

    assertThat( repositoryProvider.getCurrentRepositoryLocation() ).isEqualTo( repositoryLocation );
  }

  @Test
  public void testUseWithRegisteredRepository() {
    interpreter.execute( "use", REPO_NAME );

    assertThat( repositoryProvider.getCurrentRepositoryLocation() ).isEqualTo( repositoryLocation );
  }

  @Test
  public void testUseWithNonExistingRepositoryName() {
    interpreter.execute( "use", "other" );

    assertThat( repositoryProvider.getCurrentRepositoryLocation() ).isNull();
  }

  @Test
  public void testHelp() {
    interpreter.execute( "help" );

    verify( errorOutput, atLeastOnce() ).writeLine( any() );
  }

  private File createRepository( String name ) throws IOException {
    return consoleHelper.createRepositories( name )[ 0 ].getCanonicalFile();
  }

  private ControlCommandInterpreter createControlCommandInterpreter() {
    ConsoleOutput standardOutput = mock( ConsoleOutput.class );
    return new ControlCommandInterpreter( consoleComponentFactory, standardOutput, errorOutput, repositoryProvider );
  }
}
