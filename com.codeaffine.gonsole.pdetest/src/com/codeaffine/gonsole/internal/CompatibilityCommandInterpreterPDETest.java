package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;
import com.codeaffine.test.util.concurrency.RunInThread;

public class CompatibilityCommandInterpreterPDETest {

  @Rule public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();

  private CompatibilityCommandInterpreter interpreter;

  @Test
  public void testIsRecognizedWithGitOnly() {
    boolean recognized = interpreter.isRecognized( "git" );

    assertThat( recognized ).isFalse();
  }

  @Test
  public void testIsRecognizedWithKnownGitCommand() {
    boolean recognized = interpreter.isRecognized( "git", "status" );

    assertThat( recognized ).isTrue();
  }

  @Test
  public void testIsRecognizedWithUnknownGitCommand() {
    boolean recognized = interpreter.isRecognized( "git", "unknown" );

    assertThat( recognized ).isFalse();
  }

  @Test
  @RunInThread
  public void testExecuteFromBackgroundThread() {
    String executionResult = interpreter.execute( "git", "status" );

    assertThat( executionResult ).isNull();
  }

  @Before
  public void setUp() throws GitAPIException {
    File gitDirectory = repositories.create( "repo" )[ 0 ];
    interpreter = new CompatibilityCommandInterpreter( mock( ConsoleOutput.class ), gitDirectory );
  }
}
