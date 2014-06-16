package com.codeaffine.gonsole.internal;

import static com.codeaffine.gonsole.internal.DefaultConsoleIOProvider.ERROR_COLOR;
import static com.codeaffine.gonsole.internal.DefaultConsoleIOProvider.INPUT_COLOR;
import static com.codeaffine.gonsole.internal.DefaultConsoleIOProvider.OUTPUT_COLOR;
import static com.codeaffine.gonsole.internal.DefaultConsoleIOProvider.PROMPT_COLOR;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.pdetest.GitConsoleBot;
import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;
import com.codeaffine.test.util.swt.DisplayHelper;

public class DefaultConsoleIOProviderPDETest {

  @Rule
  public final GitConsoleBot consoleBot = new GitConsoleBot();
  @Rule
  public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();
  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void testGetLineDelimiter() throws GitAPIException {
    GitConsole console = consoleBot.open( repositories.create( "repo" ) );
    DefaultConsoleIOProvider consoleIOProvider = createConsoleIOProvider( console );

    String lineDelimiter = consoleIOProvider.getLineDelimiter();

    String expectedLineDelimiter = console.getPage().getViewer().getTextWidget().getLineDelimiter();
    assertThat( lineDelimiter ).isEqualTo( expectedLineDelimiter );
  }

  @Test
  public void testGetInputStream() throws GitAPIException {
    DefaultConsoleIOProvider consoleIOProvider = createConsoleIOProvider();

    IOConsoleInputStream inputStream = ( IOConsoleInputStream )consoleIOProvider.getInputStream();

    assertThat( inputStream.getColor() ).isEqualTo( getColor( INPUT_COLOR ) );
  }

  @Test
  public void testGetOutputStream() throws GitAPIException {
    DefaultConsoleIOProvider consoleIOProvider = createConsoleIOProvider();

    IOConsoleOutputStream outStream = ( IOConsoleOutputStream )consoleIOProvider.getOutputStream();

    assertThat( outStream.getColor() ).isEqualTo( getColor( OUTPUT_COLOR ) );
  }

  @Test
  public void testGetErrorStream() throws GitAPIException {
    DefaultConsoleIOProvider consoleIOProvider = createConsoleIOProvider();

    IOConsoleOutputStream errorStream = ( IOConsoleOutputStream )consoleIOProvider.getErrorStream();

    assertThat( errorStream.getColor() ).isEqualTo( getColor( ERROR_COLOR ) );
  }

  @Test
  public void testGetPromptStream() throws GitAPIException {
    DefaultConsoleIOProvider consoleIOProvider = createConsoleIOProvider();

    IOConsoleOutputStream promptStream = ( IOConsoleOutputStream )consoleIOProvider.getPromptStream();

    assertThat( promptStream.getColor() ).isEqualTo( getColor( PROMPT_COLOR ) );
  }

  @Test
  public void testDestroyingConsoleClosesStreams() throws GitAPIException {
    GitConsole console = consoleBot.open( repositories.create( "repo" ) );
    DefaultConsoleIOProvider consoleIOProvider = createConsoleIOProvider( console );
    IOConsoleOutputStream outStream = ( IOConsoleOutputStream )consoleIOProvider.getOutputStream();
    IOConsoleOutputStream errorStream = ( IOConsoleOutputStream )consoleIOProvider.getErrorStream();

    console.destroy();

    assertThat( outStream.isClosed() ).isTrue();
    assertThat( errorStream.isClosed() ).isTrue();
  }

  private Color getColor( int colorCode ) {
    return displayHelper.getDisplay().getSystemColor( colorCode );
  }

  private DefaultConsoleIOProvider createConsoleIOProvider() throws GitAPIException {
    GitConsole console = consoleBot.open( repositories.create( "repo" ) );
    return createConsoleIOProvider( console );
  }

  private DefaultConsoleIOProvider createConsoleIOProvider( GitConsole console ) {
    return new DefaultConsoleIOProvider( displayHelper.getDisplay(), console );
  }
}
