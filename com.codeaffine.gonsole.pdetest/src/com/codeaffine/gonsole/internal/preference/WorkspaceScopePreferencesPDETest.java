package com.codeaffine.gonsole.internal.preference;

import static com.codeaffine.gonsole.internal.preference.WorkspaceScopePreferences.PREF_REPOSITORY_LOCATIONS;
import static com.codeaffine.gonsole.internal.preference.WorkspaceScopePreferencesHelper.newWorkspaceScopePreferences;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.internal.preference.WorkspaceScopePreferences;

public class WorkspaceScopePreferencesPDETest {

  private IPreferenceStore preferenceStore;
  private WorkspaceScopePreferences preferences;

  @Test
  public void testInitializeDefaults() {
    preferences.initializeDefaults();

    assertThat( preferences.getRepositoryLocations() ).isEmpty();
    assertThat( preferenceStore.getDefaultString( PREF_REPOSITORY_LOCATIONS ) ).isEmpty();
  }

  @Test
  public void testSetRepositoryLocations() {
    String value = "foo";

    preferences.setRepositoryLocations( value );

    assertThat( preferences.getRepositoryLocations() ).isEqualTo( value );
    assertThat( preferenceStore.getString( PREF_REPOSITORY_LOCATIONS ) ).isEqualTo( value );
  }

  @Test
  public void testSetHistory() {
    preferences.setHistoryItems( "a", "b", "c" );

    assertThat( preferences.getHistoryItems() ).containsExactly( "a", "b", "c" );
  }

  @Test
  public void testGetHistory() {
    String[] historyItems = preferences.getHistoryItems();

    assertThat( historyItems ).isEmpty();
  }

  @Before
  public void setUp() {
    preferenceStore = new PreferenceStore();
    preferences = newWorkspaceScopePreferences( preferenceStore );
  }
}
