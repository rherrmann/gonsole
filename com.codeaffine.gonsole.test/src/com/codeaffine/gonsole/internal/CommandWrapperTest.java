package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.OutputStream;

import org.eclipse.jgit.lib.Repository;
import org.junit.Test;

public class CommandWrapperTest {

  @Test
  public void testInitWithJGit3_3() {
    TestCommand_JGit_3_3 command = spy( new TestCommand_JGit_3_3() );
    CommandWrapper commandWrapper = new CommandWrapper( command, TestCommand_JGit_3_3.class );
    Repository repository = mock( Repository.class );
    OutputStream outputStream = mock( OutputStream.class );

    commandWrapper.init( repository, outputStream );

    assertThat( command.outs ).isSameAs( outputStream );
    verify( command ).init( repository, null );
  }

  @Test
  public void testInitWithJGit3_4() {
    TestCommand_JGit_3_4 command = spy( new TestCommand_JGit_3_4() );
    CommandWrapper commandWrapper = new CommandWrapper( command, TestCommand_JGit_3_4.class );
    Repository repository = mock( Repository.class );
    OutputStream outputStream = mock( OutputStream.class );

    commandWrapper.init( repository, outputStream );

    assertThat( command.outs ).isSameAs( outputStream );
    assertThat( command.errs ).isSameAs( outputStream );
    verify( command ).init( repository, null );
  }

  @Test
  public void testInitWithException() {
    Repository repository = mock( Repository.class );
    TestCommand_JGit_3_3 command = spy( new TestCommand_JGit_3_3() );
    RuntimeException exception = new RuntimeException();
    doThrow( exception ).when( command ).init( repository, null );
    CommandWrapper commandWrapper = new CommandWrapper( command, TestCommand_JGit_3_3.class );

    try {
      commandWrapper.init( repository, null );
      fail( "" );
    } catch( Exception expected ) {
      assertThat( expected ).isSameAs( exception );
    }
  }

  static class TestCommand_JGit_3_4 {
    protected OutputStream outs;
    protected OutputStream errs;

    @SuppressWarnings("unused")
    protected void init( Repository repository, String gitDir ) {
    }
  }

  static class TestCommand_JGit_3_3 {
    protected OutputStream outs;

    @SuppressWarnings("unused")
    protected void init( Repository repository, String gitDir ) {
    }
  }

}
