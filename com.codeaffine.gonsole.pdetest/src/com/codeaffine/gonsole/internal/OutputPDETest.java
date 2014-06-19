package com.codeaffine.gonsole.internal;

import static com.codeaffine.gonsole.internal.resource.ConsoleIoProvider.LINE_DELIMITER;
import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.assertThat;
import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.line;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.ConsoleOutput;
import com.codeaffine.gonsole.ConsoleOutput.ConsoleWriter;
import com.codeaffine.gonsole.pdetest.GitConsoleBot;
import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;
import com.google.common.base.Charsets;

public class OutputPDETest {

  @Rule public final GitConsoleBot consoleBot = new GitConsoleBot();
  @Rule public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();

  @Test
  public void testWrite() {
    Console console = consoleBot.open( repositories.create( "repo" ) );

    ConsoleOutput consoleOutput = createConsoleOutput( console );
    consoleOutput.write( new ConsoleWriter() {
      @Override
      public void write( OutputStream outputStream ) throws IOException {
        outputStream.write( "foo".getBytes( Charsets.UTF_8 ) );
      }
    } );

    assertThat( consoleBot )
      .hasProcessedCommandLine()
      .containsLines( line( "repo", "foo" ) );
  }

  @Test
  public void testWriteText() {
    Console console = consoleBot.open( repositories.create( "repo" ) );

    ConsoleOutput consoleOutput = createConsoleOutput( console );
    consoleOutput.write( "foo" );

    assertThat( consoleBot )
      .hasProcessedCommandLine()
      .containsLines( line( "repo", "foo" ) );
  }

  private static ConsoleOutput createConsoleOutput( Console console ) {
    OutputStream outputStream = new IoStreamProvider( console ).newOutputStream( null );
    Charset encoding = Charset.forName( console.getEncoding() );
    return new Output( outputStream, encoding, LINE_DELIMITER );
  }
}