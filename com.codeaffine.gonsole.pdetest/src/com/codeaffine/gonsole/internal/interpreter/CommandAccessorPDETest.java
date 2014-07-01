package com.codeaffine.gonsole.internal.interpreter;

import static com.codeaffine.console.core.ConsoleConstants.ENCODING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.pgm.TextBuiltin;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.acceptance.GitConsoleHelper;

public class CommandAccessorPDETest {

  @Rule public final GitConsoleHelper configurer = new GitConsoleHelper();

  private Repository repository;
  private OutputStream outputStream;
  private ByteArrayOutputStream errorStream;

  @Test
  public void testInit() {
    TestCommand command = spy( new TestCommand() );
    CommandAccessor accessor = createCommandAccessor( command );

    accessor.init( repository, outputStream );

    assertThat( command.getOuts() ).isSameAs( outputStream );
    assertThat( command.getErrs() ).isSameAs( errorStream );
    verify( command ).init( repository, null );
  }

  @Test
  public void testInitWithException() {
    TestCommand command = spy( new TestCommand() );
    RuntimeException exception = new RuntimeException();
    doThrow( exception ).when( command ).init( repository, null );
    CommandAccessor accessor = createCommandAccessor( command );

    try {
      accessor.init( repository, outputStream );
      fail();
    } catch( Exception expected ) {
      assertThat( expected ).isSameAs( exception );
    }
  }

  @Test
  public  void testFlush() throws IOException {
    TestCommand command = new TestCommand();
    CommandAccessor accessor = createCommandAccessor( command );
    accessor.init( repository, outputStream );

    accessor.flush();

    verify( outputStream ).flush();
    verify( errorStream ).flush();
  }

  @Test
  public void testFlushWithException() throws IOException {
    TestCommand command = spy( new TestCommand() );
    CommandAccessor accessor = createCommandAccessor( command );
    IOException toBeThrown = new IOException();
    doThrow( toBeThrown ).when( outputStream ).flush();
    accessor.init( repository, outputStream );

    try {
      accessor.flush();
      fail();
    } catch ( RuntimeException expected ) {
      assertThat( expected.getCause() ).isSameAs( toBeThrown );
    }
  }

  @Test
  public void testExecuteTrimsExecutionResult() throws IOException {
    errorStream.write( "message\n".getBytes( ENCODING ) );
    CommandAccessor accessor = createCommandAccessor( new TestCommand() );

    accessor.init( repository, outputStream );
    String executionResult = accessor.execute();

    assertThat( executionResult ).isEqualTo( "message" );
  }

  @Test
  public void testExecuteRemovesFatalPrefixFromExecutionResult() throws IOException {
    errorStream.write( "fatal: message".getBytes( ENCODING ) );
    CommandAccessor accessor = createCommandAccessor( new TestCommand() );

    accessor.init( repository, outputStream );
    String executionResult = accessor.execute();

    assertThat( executionResult ).isEqualTo( "message" );
  }

  @Before
  public void setUp() throws Exception {
    FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
    repositoryBuilder.setGitDir( configurer.createRepositories( "repo" )[ 0 ] );
    repository = repositoryBuilder.build();
    outputStream = mock( OutputStream.class );
    errorStream = spy( new ByteArrayOutputStream() );
  }

  @After
  public void tearDown() {
    repository.close();
  }

  private CommandAccessor createCommandAccessor( TestCommand command ) {
    CommandInfo commandInfo = new CommandInfo();
    commandInfo.command = command;
    return new CommandAccessor( commandInfo, errorStream );
  }

  public static class TestCommand extends TextBuiltin {

    OutputStream getOuts() {
      return outs;
    }

    OutputStream getErrs() {
      return errs;
    }

    Writer getOutw() {
      return outw;
    }

    Writer getErrw() {
      return errw;
    }

    @Override
    protected void init( Repository repository, String gitDir ) {
      super.init( repository, gitDir );
    }

    @Override
    protected void run() throws Exception {
    }
  }
}