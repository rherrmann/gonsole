package com.codeaffine.gonsole.internal.interpreter;

import static com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper.createWithSingleChildProvider;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.gonsole.acceptance.GitConsoleHelper;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

public class ControlCommandInterpreterPDETest {

  private static final String REPO_NAME = "repo";

  @Rule
  public final GitConsoleHelper consoleHelper = new GitConsoleHelper();

  private CompositeRepositoryProvider repositoryProvider;
  private File repositoryLocation;
  private ControlCommandInterpreter interpreter;

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
  public void testUseWtihNonExistingRepositoryName() {
    interpreter.execute( "use", "other" );

    assertThat( repositoryProvider.getCurrentRepositoryLocation() ).isNull();
  }

  @Before
  public void setUp() throws IOException {
    repositoryLocation = createRepository( REPO_NAME );
    repositoryProvider = createWithSingleChildProvider( repositoryLocation );
    repositoryProvider.setCurrentRepositoryLocation( null );
    interpreter = new ControlCommandInterpreter( mock( ConsoleOutput.class ), repositoryProvider );
  }

  private File createRepository( String name ) throws IOException {
    return consoleHelper.createRepositories( name )[ 0 ].getCanonicalFile();
  }
}
