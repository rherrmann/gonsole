package com.codeaffine.gonsole.internal.interpreter;

import static com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper.createWithSingleChildProvider;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.gonsole.acceptance.ConsoleHelper;
import com.codeaffine.gonsole.internal.interpreter.GitCommandInterpreter;
import com.codeaffine.gonsole.internal.interpreter.PgmResourceBundle;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.test.util.concurrency.RunInThread;
import com.codeaffine.test.util.concurrency.RunInThreadRule;


public class GitCommandInterpreterPDETest {

  @Rule public final RunInThreadRule runInThreadRule = new RunInThreadRule();
  @Rule public final ConsoleHelper configurer = new ConsoleHelper();

  private GitCommandInterpreter interpreter;

  @Test
  @RunInThread
  public void testIsRecognizedFromBackgroundThread() {
    boolean recognized = interpreter.isRecognized( "status" );

    assertThat( recognized ).isTrue();
  }

  @Test
  @RunInThread
  public void testExecuteFromBackgroundThread() {
    String executionResult = interpreter.execute( "status" );

    assertThat( executionResult ).isNull();
  }

  @Before
  public void setUp() {
    new PgmResourceBundle().reset();
    File gitDirectory = configurer.createRepositories( "repo" )[ 0 ];
    CompositeRepositoryProvider repositoryProvider = createWithSingleChildProvider( gitDirectory );
    interpreter = new GitCommandInterpreter( mock( ConsoleOutput.class ), repositoryProvider );
  }
}
