package com.codeaffine.gonsole.internal.preference;

import static com.google.common.collect.Iterables.toArray;

import org.eclipse.jface.preference.IPreferenceStore;

import com.codeaffine.gonsole.internal.activator.GonsolePlugin;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class WorkspaceScopePreferences {

  private static final char SEPARATOR = '\u0000';
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
    String joinedItems = Joiner.on( SEPARATOR ).join( items );
    preferenceStore.setValue( PREF_HISTORY, joinedItems );
  }

  public String[] getHistoryItems() {
    String preferenceValue = preferenceStore.getString( PREF_HISTORY );
    Iterable<String> items = Splitter.on( SEPARATOR ).omitEmptyStrings().split( preferenceValue );
    return toArray( items, String.class );
  }
}