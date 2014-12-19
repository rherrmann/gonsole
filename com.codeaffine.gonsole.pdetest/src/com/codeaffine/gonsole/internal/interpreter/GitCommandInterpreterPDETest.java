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

  private GitCommandInterpreter interpreter;

  @Test
  public void testIsRecognizedForKnownCommand() {
    boolean recognized = interpreter.isRecognized( "status" );

    assertThat( recognized ).isTrue();
  }

  @Test
  public void testIsRecognizedForKnownCommandWithInvalidArguments() {
    boolean recognized = interpreter.isRecognized( "status", "invalid", "argument" );

    assertThat( recognized ).isTrue();
  }

  @Test
  public void testIsRecognizedForUnknownCommand() {
    boolean recognized = interpreter.isRecognized( "unknown" );

    assertThat( recognized ).isFalse();
  }

  @Test
  @RunInThread
  public void testIsRecognizedFromBackgroundThread() {
    boolean recognized = interpreter.isRecognized( "status" );

    assertThat( recognized ).isTrue();
  }

  @Test
  public void testExecuteForKnownCommand() {
    String executionResult = interpreter.execute( "status" );

    assertThat( executionResult ).isNull();
  }

  @Test
  public void testExecuteForKnownCommandWithInvalidArguments() {
    String executionResult = interpreter.execute( "status", "invalid", "argument" );

    assertThat( executionResult ).isNull();
  }

  @Test
  @RunInThread
  public void testExecuteFromBackgroundThread() {
    String executionResult = interpreter.execute( "status" );

    assertThat( executionResult ).isNull();
  }

  @Before
  public void setUp() {
    File gitDirectory = configurer.createRepositories( "repo" )[ 0 ];
    CompositeRepositoryProvider repositoryProvider = createWithSingleChildProvider( gitDirectory );
    interpreter = new GitCommandInterpreter( mock( ConsoleOutput.class ), repositoryProvider );
  }
}
