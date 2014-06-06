package com.codeaffine.gonsole.internal.preference;

import static com.codeaffine.gonsole.internal.preference.WorkspaceScopePreferences.PREF_REPOSITORY_LOCATIONS;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.junit.Before;
import org.junit.Test;

public class WorkspaceScopePreferencesPDETest {

  private IPreferenceStore preferenceStore;
  private WorkspaceScopePreferences preferences;

  @Test
  public void testGetPreferenceStore() {
    assertThat( preferences.getPreferenceStore() ).isSameAs( preferenceStore );
  }

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

  @Before
  public void setUp() {
    preferenceStore = new PreferenceStore();
    preferences = new WorkspaceScopePreferences( preferenceStore );
  }
}
