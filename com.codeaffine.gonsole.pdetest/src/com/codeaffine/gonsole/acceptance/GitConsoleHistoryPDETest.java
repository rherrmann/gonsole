package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;

import org.eclipse.swt.SWT;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.test.util.ConditionalIgnoreRule;
import com.codeaffine.test.util.GtkPlatform;
import com.codeaffine.test.util.ConditionalIgnoreRule.ConditionalIgnore;


public class GitConsoleHistoryPDETest {

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final ConsoleBot console = new ConsoleBot();
  @Rule public final GitConsoleHelper configurer = new GitConsoleHelper();

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
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
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testOpenHistoryBeforeAnyCommandWasEntered() {
    console.open( configurer.createConfigurer( "repo" ) );

    console.typeKey( SWT.NONE, SWT.ARROW_UP );

    assertThat( console ).showsNoContentAssist();
  }
}
