package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.gonsole.internal.gitconsole.preference.WorkspaceScopePreferencesHelper.newWorkspaceScopePreferences;
import static org.eclipse.core.runtime.preferences.InstanceScope.INSTANCE;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.codeaffine.gonsole.internal.gitconsole.preference.RepositoryPreferencePage;
import com.codeaffine.gonsole.internal.gitconsole.preference.WorkspaceScopePreferences;

class RepositoryPreferencePageHelper {

  private final RepositoryPreferencePage preferencePage;
  private final WorkspaceScopePreferences preferences;

  RepositoryPreferencePageHelper( Composite parent ) {
    IPreferenceStore preferenceStore = createPreferenceStore();
    preferencePage = new RepositoryPreferencePage();
    preferencePage.setPreferenceStore( preferenceStore );
    preferencePage.createControl( parent );
    preferences = createWorkspaceScopePreferences( preferenceStore );
  }

  String enterRepositoryLocations( String firstLocation, String ... locations ) {
    String result = firstLocation;
    Text textControl = preferencePage.getRepositoryLocationsControl();
    for( String location : locations ) {
      result += textControl.getLineDelimiter() + location;
    }
    textControl.setText( result );
    return result;
  }

  void dispose() {
    preferencePage.dispose();
  }

  void pressOk() {
    preferencePage.performOk();
  }

  void pressCancel() {
    preferencePage.performCancel();
  }

  WorkspaceScopePreferences getWorkspaceScopePreferences() {
    return preferences;
  }

  private static IPreferenceStore createPreferenceStore() {
    String qualifier = "com.codeaffine.gonsole.pdetest-" + System.currentTimeMillis();
    return new ScopedPreferenceStore( INSTANCE, qualifier );
  }

  private static WorkspaceScopePreferences createWorkspaceScopePreferences( IPreferenceStore preferenceStore ) {
    WorkspaceScopePreferences result = newWorkspaceScopePreferences( preferenceStore );
    result.initializeDefaults();
    return result;
  }
}