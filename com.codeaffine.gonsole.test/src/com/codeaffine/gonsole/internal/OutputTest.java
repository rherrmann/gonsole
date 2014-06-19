package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsoleOutput.ConsoleWriter;
import com.google.common.base.Charsets;


public class OutputTest {

  private static final String LINE_DELIMITER = "\n";
  private static final Charset ENCODING = Charsets.UTF_8;

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
    consoleOutput = new Output( out, ENCODING, LINE_DELIMITER );
  }

  private ConsoleWriter mockConsoleWriterWithProblem( IOException ioException ) throws IOException {
    ConsoleWriter result = mock( ConsoleWriter.class );
    doThrow( ioException ).when( result ).write( out );
    return result;
  }
}