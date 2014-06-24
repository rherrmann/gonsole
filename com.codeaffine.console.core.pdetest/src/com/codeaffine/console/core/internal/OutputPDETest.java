package com.codeaffine.console.core.internal;

import static com.codeaffine.console.core.ConsoleConstants.ENCODING;
import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.line;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsoleOutput.ConsoleWriter;
import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.console.core.pdetest.console.TestConsoleDefinition;

public class OutputPDETest {

  @Rule public final ConsoleBot consoleBot = new ConsoleBot();

  @Test
  public void testWrite() {
    Console console = consoleBot.open( new TestConsoleDefinition() );

    ConsoleOutput consoleOutput = createConsoleOutput( console );
    consoleOutput.write( new ConsoleWriter() {
      @Override
      public void write( OutputStream outputStream ) throws IOException {
        outputStream.write( "foo".getBytes( ENCODING ) );
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
    return new Output( new IoStreamProvider( console ).newOutputStream( null ) );
  }
}