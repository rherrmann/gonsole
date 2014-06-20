package com.codeaffine.gonsole.internal.preference;

import org.eclipse.jface.preference.IPreferenceStore;

import com.codeaffine.gonsole.internal.preference.WorkspaceScopePreferences;

public class WorkspaceScopePreferencesHelper {

  public static WorkspaceScopePreferences newWorkspaceScopePreferences( IPreferenceStore preferenceStore ) {
    return new WorkspaceScopePreferences( preferenceStore );
  }
}