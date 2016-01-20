package com.codeaffine.gonsole.internal.interpreter;

import static com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper.createWithSingleChildProvider;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.gonsole.acceptance.GitConsoleHelper;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.test.util.util.concurrent.RunInThread;
import com.codeaffine.test.util.util.concurrent.RunInThreadRule;


public class GitCommandInterpreterPDETest {

  @Rule public final RunInThreadRule runInThreadRule = new RunInThreadRule();
  @Rule public final GitConsoleHelper configurer = new GitConsoleHelper();

  private CompositeRepositoryProvider repositoryProvider;
  @Before
  public void setUp() {
    File gitDirectory = configurer.createRepositories( "repo" )[ 0 ];
    repositoryProvider = createWithSingleChildProvider( gitDirectory );
  }

  @Test
  public void testIsRecognizedForKnownCommand() {
    GitCommandInterpreter interpreter = createInterpreter();

    boolean recognized = interpreter.isRecognized( "status" );

    assertThat( recognized ).isTrue();
  }

  @Test
  public void testIsRecognizedForKnownCommandWithInvalidArguments() {
    GitCommandInterpreter interpreter = createInterpreter();

    boolean recognized = interpreter.isRecognized( "status", "invalid", "argument" );

    assertThat( recognized ).isTrue();
  }

  @Test
  public void testIsRecognizedForUnknownCommand() {
    GitCommandInterpreter interpreter = createInterpreter();

    boolean recognized = interpreter.isRecognized( "unknown" );

    assertThat( recognized ).isFalse();
  }

  @Test
  public void testIsRecognizedForUnknownCommandWhileNoRepositoryInUse() {
    repositoryProvider.setCurrentRepositoryLocation( null );
    GitCommandInterpreter interpreter = createInterpreter();

    boolean recognized = interpreter.isRecognized( "unknown" );

    assertThat( recognized ).isFalse();
  }

  @Test
  @RunInThread
  public void testIsRecognizedFromBackgroundThread() {
    GitCommandInterpreter interpreter = createInterpreter();

    boolean recognized = interpreter.isRecognized( "status" );

    assertThat( recognized ).isTrue();
  }

  @Test
  public void testExecuteForKnownCommand() {
    GitCommandInterpreter interpreter = createInterpreter();

    String executionResult = interpreter.execute( "status" );

    assertThat( executionResult ).isNull();
  }

  @Test
  public void testExecuteForKnownCommandWithInvalidArguments() {
    GitCommandInterpreter interpreter = createInterpreter();

    String executionResult = interpreter.execute( "status", "invalid", "argument" );

    assertThat( executionResult ).isNull();
  }

  @Test
  @RunInThread
  public void testExecuteFromBackgroundThread() {
    GitCommandInterpreter interpreter = createInterpreter();

    String executionResult = interpreter.execute( "status" );

    assertThat( executionResult ).isNull();
  }

  private GitCommandInterpreter createInterpreter() {
    ConsoleOutput consoleOutput = mock( ConsoleOutput.class );
    File currentRepositoryLocation = repositoryProvider.getCurrentRepositoryLocation();
    CommandExecutor commandExecutor = new CommandExecutor( consoleOutput, currentRepositoryLocation );
    return new GitCommandInterpreter( commandExecutor, new CommandLineParser() );
  }
}
