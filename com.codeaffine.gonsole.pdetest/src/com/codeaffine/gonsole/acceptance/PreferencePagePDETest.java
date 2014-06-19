package com.codeaffine.gonsole.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.internal.gitconsole.preference.WorkspaceScopePreferences;
import com.codeaffine.test.util.swt.DisplayHelper;

public class PreferencePagePDETest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private RepositoryPreferencePageHelper pageHelper;
  private WorkspaceScopePreferences preferences;

  @Test
  public void testEnterRepositoryLocationsAndPerformOk() {
    String expected = pageHelper.enterRepositoryLocations( "/path/to/repo1", "/path/to/repo2" );

    pageHelper.pressOk();

    assertThat( preferences.getRepositoryLocations() ).isEqualTo( expected );
  }

  @Test
  public void testEnterRepositoryLocationsAndPerformCancel() {
    pageHelper.enterRepositoryLocations( "/path/to/repo" );

    pageHelper.pressCancel();

    assertThat( preferences.getRepositoryLocations() ).isEmpty();
  }

  @Before
  public void setUp() {
    pageHelper = new RepositoryPreferencePageHelper( displayHelper.createShell() );
    preferences = pageHelper.getWorkspaceScopePreferences();
  }

  @After
  public void tearDown() {
    pageHelper.dispose();
  }
}