package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static com.codeaffine.gonsole.acceptance.GitConsolePrompts.line;

import java.io.IOException;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.gonsole.internal.GitConsoleConfigurer;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.pdetest.DefaultLocaleRule;
import com.codeaffine.test.util.GtkPlatform;
import com.codeaffine.test.util.ConditionalIgnoreRule.ConditionalIgnore;

public class GitConsoleAliasPDETest {

  @Rule public final DefaultLocaleRule defaultLocaleRule = new DefaultLocaleRule( Locale.ENGLISH );
  @Rule public final GitConsoleHelper configurer = new GitConsoleHelper();
  @Rule public final ConsoleBot consoleBot = new ConsoleBot();

  private GitConsoleConfigurer consoleConfigurer;
  private ConfigHelper configHelper;

  @Test
  public void testEnterCommandAlias() throws IOException {
    configHelper.setValue( "alias", "st", "status" );
    consoleBot.open( consoleConfigurer );

    consoleBot.enterCommandLine( "st" );

    assertThat( consoleBot )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "st" ), "# On branch master", line( "repo" ) );
  }

  @Test
  public void testEnterCommandAliasWithGitPrefix() throws IOException {
    configHelper.setValue( "alias", "st", "status" );
    consoleBot.open( consoleConfigurer );

    consoleBot.enterCommandLine( "git st" );

    assertThat( consoleBot )
      .hasProcessedCommandLine()
      .caretIsAtEnd()
      .containsLines( line( "repo", "git st" ), "# On branch master", line( "repo" ) );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testAdditionalInfoForAlias() throws IOException {
    configHelper.setValue( "alias", "co", "checkout" );
    consoleBot.open( consoleConfigurer );

    consoleBot.typeText( "co" );
    consoleBot.typeKey( SWT.CTRL, ' ' );

    assertThat( consoleBot )
      .showsAdditionalInfo()
      .containsText( "Alias for", "checkout" );
  }

 @Before
  public void setUp() {
    consoleConfigurer = ( GitConsoleConfigurer )configurer.createConfigurer( "repo" );
    CompositeRepositoryProvider repositoryProvider = consoleConfigurer.getRepositoryProvider();
    configHelper = new ConfigHelper( repositoryProvider.getRepositoryLocations()[ 0 ] );
  }
}