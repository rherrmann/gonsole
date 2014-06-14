package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.codeaffine.gonsole.internal.ConsoleOutput.ConsoleWriter;
import com.google.common.base.Charsets;


public class ConsoleOutputTest {

  private OutputStream out;
  private ConsoleIOProvider consoleIOProvider;
  private ConsoleOutput consoleOutput;

  @Test
  public void testWriteText() throws IOException {
    consoleOutput.write( "foo" );

    InOrder order = inOrder( consoleIOProvider, out );
    order.verify( consoleIOProvider ).newOutputStream();
    order.verify( out ).write( "foo".getBytes( consoleIOProvider.getEncoding() ) );
    order.verify( out ).close();
  }

  @Test
  public void testWrite() throws IOException {
    ConsoleWriter consoleWriter = mock( ConsoleWriter.class );

    consoleOutput.write( consoleWriter );

    InOrder order = inOrder( consoleIOProvider, out, consoleWriter );
    order.verify( consoleIOProvider ).newOutputStream();
    order.verify( consoleWriter ).write( out );
    order.verify( out ).close();
  }

  @Test
  public void testWriteLine() throws IOException {
    consoleOutput.writeLine( "foo" );

    InOrder order = inOrder( consoleIOProvider, out );
    order.verify( consoleIOProvider ).newOutputStream();
    order.verify( out ).write( "foo\n".getBytes( consoleIOProvider.getEncoding() ) );
    order.verify( out ).close();
  }

  @Test
  public void testWriteWithException() throws IOException {
    IOException ioException = new IOException();
    ConsoleWriter consoleWriter = mockConsoleWriterWithProblem( ioException );

    try {
      consoleOutput.write( consoleWriter );
      fail();
    } catch( RuntimeException expected ) {
      assertThat( expected.getCause() ).isSameAs( ioException );
    }

    verify( out ).close();
  }

  @Before
  public void setUp() {
    out = mock( OutputStream.class );
    consoleIOProvider = mockConsoleIoProvider( out );
    consoleOutput = new ConsoleOutput( consoleIOProvider );
  }

  private static ConsoleIOProvider mockConsoleIoProvider( OutputStream out ) {
    ConsoleIOProvider result = mock( ConsoleIOProvider.class );
    when( result.newOutputStream() ).thenReturn( out );
    when( result.getEncoding() ).thenReturn( Charsets.UTF_8.name() );
    when( result.getLineDelimiter() ).thenReturn( "\n" );
    return result;
  }

  private ConsoleWriter mockConsoleWriterWithProblem( IOException ioException ) throws IOException {
    ConsoleWriter result = mock( ConsoleWriter.class );
    doThrow( ioException ).when( result ).write( out );
    return result;
  }
}