package com.codeaffine.gonsole.internal.history;

import com.codeaffine.console.core.history.HistoryStore;
import com.codeaffine.gonsole.internal.preference.WorkspaceScopePreferences;


public class PreferenceHistoryStore implements HistoryStore {

  private final WorkspaceScopePreferences preferences;

  public PreferenceHistoryStore() {
    preferences = new WorkspaceScopePreferences();
  }

  @Override
  public void setItems( String... items ) {
    preferences.setHistoryItems( items );
  }

  @Override
  public String[] getItems() {
    return preferences.getHistoryItems();
  }

  @Override
  public void clearItems() {
    preferences.setHistoryItems( new String[ 0 ] );
  }
}
