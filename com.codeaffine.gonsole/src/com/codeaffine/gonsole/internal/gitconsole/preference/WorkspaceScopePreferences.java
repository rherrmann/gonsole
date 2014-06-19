package com.codeaffine.gonsole.internal.gitconsole.preference;

import org.eclipse.jface.preference.IPreferenceStore;

import com.codeaffine.gonsole.internal.activator.GonsolePlugin;

public class WorkspaceScopePreferences {

  public static final String PREF_REPOSITORY_LOCATIONS = "repository_locations";

  private final IPreferenceStore preferenceStore;

  public WorkspaceScopePreferences() {
    this( GonsolePlugin.getInstance().getPreferenceStore() );
  }

  WorkspaceScopePreferences( IPreferenceStore preferenceStore ) {
    this.preferenceStore = preferenceStore;
  }

  public void initializeDefaults() {
    preferenceStore.setDefault( PREF_REPOSITORY_LOCATIONS, "" );
  }

  public void setRepositoryLocations( String value ) {
    preferenceStore.setValue( PREF_REPOSITORY_LOCATIONS, value );
  }

  public String getRepositoryLocations() {
    return preferenceStore.getString( PREF_REPOSITORY_LOCATIONS );
  }
}