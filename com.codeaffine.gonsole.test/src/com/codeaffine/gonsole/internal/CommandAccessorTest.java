package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

  @Test
  public void testInitWithJGit3_3() {
    TestCommand_JGit_3_3 command = spy( new TestCommand_JGit_3_3() );
    CommandAccessor commandWrapper = new CommandAccessor( command, TestCommand_JGit_3_3.class );

    commandWrapper.init( repository, outputStream );

    assertThat( command.outs ).isSameAs( outputStream );
    verify( command ).init( repository, null );
  }

  @Test
  public void testInitWithJGit3_4() {
    TestCommand_JGit_3_4 command = spy( new TestCommand_JGit_3_4() );
    CommandAccessor commandWrapper = new CommandAccessor( command, TestCommand_JGit_3_4.class );

    commandWrapper.init( repository, outputStream );

    assertThat( command.outs ).isSameAs( outputStream );
    assertThat( command.errs ).isSameAs( outputStream );
    verify( command ).init( repository, null );
  }

  @Test
  public void testInitWithException() {
    TestCommand_JGit_3_3 command = spy( new TestCommand_JGit_3_3() );
    RuntimeException exception = new RuntimeException();
    doThrow( exception ).when( command ).init( repository, null );
    CommandAccessor commandWrapper = new CommandAccessor( command, TestCommand_JGit_3_3.class );

    try {
      commandWrapper.init( repository, outputStream );
      fail();
    } catch( Exception expected ) {
      assertThat( expected ).isSameAs( exception );
    }
  }

  @Test
  public  void testFlushWithJGit_3_4() throws IOException {
    TestCommand_JGit_3_4 command = spy( new TestCommand_JGit_3_4() );
    CommandAccessor commandWrapper = new CommandAccessor( command, TestCommand_JGit_3_4.class );
    commandWrapper.init( repository, outputStream );

    commandWrapper.flush();

    verify( outputStream, times( 2 ) ).flush();
  }

  @Test
  public void testFlushWithException() throws IOException {
    TestCommand_JGit_3_4 command = spy( new TestCommand_JGit_3_4() );
    CommandAccessor commandWrapper = new CommandAccessor( command, TestCommand_JGit_3_4.class );
    IOException toBeThrown = new IOException();
    doThrow( toBeThrown ).when( outputStream ).flush();
    commandWrapper.init( repository, outputStream );

    try {
      commandWrapper.flush();
      fail();
    } catch ( RuntimeException expected ) {
      assertThat( expected.getCause() ).isSameAs( toBeThrown );
    }
  }

  @Test
  public  void testFlushWithJGit_3_3() throws IOException {
    TestCommand_JGit_3_3 command = spy( new TestCommand_JGit_3_3() );
    CommandAccessor commandWrapper = new CommandAccessor( command, TestCommand_JGit_3_3.class );
    commandWrapper.init( repository, outputStream );

    commandWrapper.flush();

    verify( outputStream, times( 1 ) ).flush();
  }

  @Before
  public void setUp() {
    repository = mock( Repository.class );
    outputStream = mock( OutputStream.class );
  }

  static class TestCommand_JGit_3_4 {
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

  static class TestCommand_JGit_3_3 {
    protected OutputStream outs;
    protected Writer outw;

    @SuppressWarnings("unused")
    protected void init( Repository repository, String gitDir ) {
      outw = new OutputStreamWriter( outs );
    }
  }
}