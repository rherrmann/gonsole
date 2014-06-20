package com.codeaffine.console.core.internal;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.line;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsoleOutput.ConsoleWriter;
import com.codeaffine.console.core.internal.resource.ConsoleIoProvider;
import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.console.core.pdetest.console.TestConsoleDefinition;
import com.google.common.base.Charsets;

public class OutputPDETest {

  @Rule public final ConsoleBot consoleBot = new ConsoleBot();

  @Test
  public void testWrite() {
    Console console = consoleBot.open( new TestConsoleDefinition() );

    ConsoleOutput consoleOutput = createConsoleOutput( console );
    consoleOutput.write( new ConsoleWriter() {
      @Override
      public void write( OutputStream outputStream ) throws IOException {
        outputStream.write( "foo".getBytes( Charsets.UTF_8 ) );
      }
    } );

    assertThat( consoleBot )
      .hasProcessedCommandLine()
      .containsLines( line( "foo" ) );
  }

  @Test
  public void testWriteText() {
    Console console = consoleBot.open( new TestConsoleDefinition() );

    ConsoleOutput consoleOutput = createConsoleOutput( console );
    consoleOutput.write( "foo" );

    assertThat( consoleBot )
      .hasProcessedCommandLine()
      .containsLines( line( "foo" ) );
  }

  private static ConsoleOutput createConsoleOutput( Console console ) {
    OutputStream outputStream = new IoStreamProvider( console ).newOutputStream( null );
    Charset encoding = Charset.forName( console.getEncoding() );
    return new Output( outputStream, encoding, ConsoleIoProvider.LINE_DELIMITER );
  }
}