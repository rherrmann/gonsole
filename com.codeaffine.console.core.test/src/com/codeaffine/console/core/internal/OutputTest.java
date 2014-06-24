package com.codeaffine.console.core.internal;

import static com.codeaffine.console.core.ConsoleConstants.ENCODING;
import static com.codeaffine.console.core.ConsoleConstants.LINE_DELIMITER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsoleOutput.ConsoleWriter;


public class OutputTest {

  private OutputStream out;
  private ConsoleOutput consoleOutput;

  @Test
  public void testWriterLeavesOutputStreamOpen() throws IOException {
    consoleOutput.write( mock( ConsoleWriter.class ) );

    verify( out, never() ).close();
  }

  @Test
  public void testWriteText() throws IOException {
    consoleOutput.write( "foo" );

    verify( out ).write( "foo".getBytes( ENCODING ) );
  }

  @Test
  public void testWrite() throws IOException {
    ConsoleWriter consoleWriter = mock( ConsoleWriter.class );

    consoleOutput.write( consoleWriter );

    verify( consoleWriter ).write( out );
  }

  @Test
  public void testWriteLine() throws IOException {
    consoleOutput.writeLine( "foo" );

    verify( out ).write( ( "foo" + LINE_DELIMITER ).getBytes( ENCODING ) );
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
  }

  @Test
  public void testWriteUsesSpecifiedEncoding() throws IOException {
    consoleOutput.write( "äöü" );

    verify( out ).write( "äöü".getBytes( ENCODING ) );
  }

  @Before
  public void setUp() {
    out = mock( OutputStream.class );
    consoleOutput = new Output( out );
  }

  private ConsoleWriter mockConsoleWriterWithProblem( IOException ioException ) throws IOException {
    ConsoleWriter result = mock( ConsoleWriter.class );
    doThrow( ioException ).when( result ).write( out );
    return result;
  }
}