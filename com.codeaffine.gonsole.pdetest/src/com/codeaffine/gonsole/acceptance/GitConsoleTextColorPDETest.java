package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.assertThat;
import static com.codeaffine.gonsole.pdetest.GitConsoleAssert.line;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.pdetest.GitConsoleBot;
import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;


public class GitConsoleTextColorPDETest {

  private static final int NEXT_LINE_OFFSET = 3;

  @Rule public final GitConsoleBot console = new GitConsoleBot();
  @Rule public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();

  @Test
  public void testPromptColor() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    assertThat( console ).hasPromptColorAt( 0 );
  }

  @Test
  public void testInputColor() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .hasInputColorAt( line( "repo", "status" ).length() );
  }

  @Test
  public void testOutputColor() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "status" );

    assertThat( console )
      .hasProcessedCommandLine()
      .hasOutputColorAt( line( "repo", "status" ).length() + NEXT_LINE_OFFSET );
  }

  @Test
  public void testErrorColor() throws GitAPIException {
    console.open( repositories.create( "repo" ) );

    console.enterCommandLine( "foo" );

    assertThat( console )
      .hasProcessedCommandLine()
      .hasErrorColorAt( line( "repo", "foo" ).length() + NEXT_LINE_OFFSET );
  }
}
