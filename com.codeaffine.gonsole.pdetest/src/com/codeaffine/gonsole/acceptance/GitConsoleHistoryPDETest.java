package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;

import org.eclipse.swt.SWT;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;


public class GitConsoleHistoryPDETest {

  @Rule public final ConsoleBot console = new ConsoleBot();
  @Rule public final GitConsoleHelper configurer = new GitConsoleHelper();

  @Test
  public void testOpenHistoryAfterCommandWasEntered() {
    console.open( configurer.createConfigurer( "repo" ) );
    console.enterCommandLine( "status" );
    console.waitForCommandLineProcessing();

    console.typeKey( SWT.NONE, SWT.ARROW_UP );

    assertThat( console )
      .showsContentAssist()
      .withProposal( "status" );
  }

  @Test
  public void testOpenHistoryBeforeAnyCommandWasEntered() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeKey( SWT.NONE, SWT.ARROW_UP );

    assertThat( console ).showsNoContentAssist();
  }
}
