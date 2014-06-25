package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.ui.part.ViewPart;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.ViewHelper;
import com.codeaffine.console.core.pdetest.bot.ConsoleBot;


public class GitConsoleTitlePDETest {

  @Rule public final ConsoleHelper configurer = new ConsoleHelper();
  @Rule public final ConsoleBot consoleBot = new ConsoleBot();

  @Test
  public void testInitialConsoleViewTitle() {
    consoleBot.open( configurer.createConfigurer( "repo" ) );

    assertThat( getConsoleViewContentDescription() ).isEqualTo( "Git Console: repo" );
  }

  @Test
  public void testConsoleViewTitleAfterChangingCurrentRepository() {
    consoleBot.open( configurer.createConfigurer( "repo-1", "repo-2" ) );

    consoleBot.enterCommandLine( "use repo-2" );

    assertThat( consoleBot ).hasProcessedCommandLine();
    assertThat( getConsoleViewContentDescription() ).isEqualTo( "Git Console: repo-2" );
  }

  private static String getConsoleViewContentDescription() {
    ViewPart consoleView = new ViewHelper().showView( ConsoleBot.CONSOLE_VIEW_ID );
    return consoleView.getContentDescription();
  }
}
