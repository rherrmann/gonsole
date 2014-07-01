package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.gonsole.acceptance.GitConsolePrompts.line;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;


public class GitConsoleTextColorPDETest {

  private static final int NEXT_LINE_OFFSET = 3;

  @Rule public final GitConsoleHelper configurer = new GitConsoleHelper();
  @Rule public final ConsoleBot console = new ConsoleBot();

  @Test
  public void testPromptColor() {
    console.open( configurer.createConfigurer( "repo" ) );

    assertThat( console ).hasPromptColorAt( 0 );
  }

  @Test
  public void testInputColor() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.enterCommandLine( "status" );

    assertThat( console )
      .hasInputColorAt( line( "repo", "status" ).length() );
  }

  @Test
  public void testOutputColor() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.enterCommandLine( "status" );

    assertThat( console )
      .hasOutputColorAt( line( "repo", "status" ).length() + NEXT_LINE_OFFSET );
  }

  @Test
  public void testErrorColor() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.enterCommandLine( "foo" );

    assertThat( console )
      .hasErrorColorAt( line( "repo", "foo" ).length() + NEXT_LINE_OFFSET );
  }
}
