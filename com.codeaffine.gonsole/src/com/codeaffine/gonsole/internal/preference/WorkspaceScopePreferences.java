package com.codeaffine.gonsole.internal.preference;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jface.preference.IPreferenceStore;

import com.codeaffine.gonsole.internal.activator.GonsolePlugin;

public class WorkspaceScopePreferences {

  private static final String SEPARATOR = "\u0000";
  public static final String PREF_REPOSITORY_LOCATIONS = "repository_locations";
  private static final String PREF_HISTORY = "history";

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

  public void setHistoryItems( String... items ) {
    String joinedItems = Stream.of( items ).collect( Collectors.joining( SEPARATOR ) );
    preferenceStore.setValue( PREF_HISTORY, joinedItems );
  }

  public String[] getHistoryItems() {
    String preferenceValue = preferenceStore.getString( PREF_HISTORY );
    String[] items = preferenceValue.split( SEPARATOR );
    return Stream.of( items ).filter( string -> !string.isEmpty() ).toArray( String[]::new );
  }
}