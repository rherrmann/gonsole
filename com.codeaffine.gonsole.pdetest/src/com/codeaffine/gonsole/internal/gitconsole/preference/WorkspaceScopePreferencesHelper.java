package com.codeaffine.gonsole.internal.gitconsole.preference;

import org.eclipse.jface.preference.IPreferenceStore;

public class WorkspaceScopePreferencesHelper {

  public static WorkspaceScopePreferences newWorkspaceScopePreferences( IPreferenceStore preferenceStore ) {
    return new WorkspaceScopePreferences( preferenceStore );
  }
}