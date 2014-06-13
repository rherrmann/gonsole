package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.eclipse.jgit.lib.Repository;
import org.junit.Before;
import org.junit.Test;

public class CommandAccessorTest {

  private Repository repository;
  private OutputStream outputStream;
  private ByteArrayOutputStream errorStream;

  @Test
  public void testInit() {
    TestCommand command = spy( new TestCommand() );
    CommandAccessor accessor = new CommandAccessor( command, TestCommand.class, errorStream );

    accessor.init( repository, outputStream );

    assertThat( command.outs ).isSameAs( outputStream );
    assertThat( command.errs ).isSameAs( accessor.errorStream );
    verify( command ).init( repository, null );
  }

  @Test
  public void testInitWithException() {
    TestCommand command = spy( new TestCommand() );
    RuntimeException exception = new RuntimeException();
    doThrow( exception ).when( command ).init( repository, null );
    CommandAccessor accessor = new CommandAccessor( command, TestCommand.class, errorStream );

    try {
      accessor.init( repository, outputStream );
      fail();
    } catch( Exception expected ) {
      assertThat( expected ).isSameAs( exception );
    }
  }

  @Test
  public  void testFlush() throws IOException {
    TestCommand command = spy( new TestCommand() );
    CommandAccessor accessor = new CommandAccessor( command, TestCommand.class, errorStream );
    accessor.init( repository, outputStream );

    accessor.flush();

    verify( outputStream ).flush();
    verify( errorStream ).flush();
  }

  @Test
  public void testFlushWithException() throws IOException {
    TestCommand command = spy( new TestCommand() );
    CommandAccessor accessor = new CommandAccessor( command, TestCommand.class, errorStream );
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

  @Before
  public void setUp() {
    repository = mock( Repository.class );
    outputStream = mock( OutputStream.class );
    errorStream = mock( ByteArrayOutputStream.class );
  }

  static class TestCommand {
    protected OutputStream outs;
    protected OutputStream errs;
    protected Writer outw;
    protected Writer errw;

    @SuppressWarnings("unused")
    protected void init( Repository repository, String gitDir ) {
      outw = new OutputStreamWriter( outs );
      errw = new OutputStreamWriter( errs );
    }
  }
}