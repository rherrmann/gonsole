package com.codeaffine.gonsole.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.swt.widgets.Display;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.internal.ConsoleOutput.ConsoleWriter;
import com.codeaffine.gonsole.pdetest.GitConsoleAssert;
import com.codeaffine.gonsole.pdetest.GitConsoleBot;
import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;
import com.google.common.base.Charsets;

public class ConsoleOutputPDETest {

  @Rule public final GitConsoleBot consoleBot = new GitConsoleBot();
  @Rule public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();

  @Test
  public void testWrite() throws GitAPIException {
    GitConsole console = consoleBot.open( repositories.create( "repo" ) );

    ConsoleOutput consoleOutput = createConsoleOutput( console );
    consoleOutput.write( new ConsoleWriter() {
      @Override
      public void write( OutputStream outputStream ) throws IOException {
        outputStream.write( "foo".getBytes( Charsets.UTF_8 ) );
      }
    } );

    GitConsoleAssert.assertThat( consoleBot )
      .hasProcessedCommandLine()
      .containsLines( "repo>foo" );
  }

  @Test
  public void testWriteText() throws GitAPIException {
    GitConsole console = consoleBot.open( repositories.create( "repo" ) );

    ConsoleOutput consoleOutput = createConsoleOutput( console );
    consoleOutput.write( "foo" );

    GitConsoleAssert.assertThat( consoleBot )
      .hasProcessedCommandLine()
      .containsLines( "repo>foo" );
  }

  private static ConsoleOutput createConsoleOutput( GitConsole console ) {
    Display display = Display.getCurrent();
    DefaultConsoleIOProvider consoleIOProvider = new DefaultConsoleIOProvider( display, console );
    OutputStream outputStream = consoleIOProvider.getOutputStream();
    Charset encoding = consoleIOProvider.getCharacterEncoding();
    String lineDelimiter = consoleIOProvider.getLineDelimiter();
    return new ConsoleOutput( outputStream, encoding, lineDelimiter );
  }
}
