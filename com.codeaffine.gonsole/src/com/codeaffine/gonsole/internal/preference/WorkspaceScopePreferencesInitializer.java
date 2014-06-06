package com.codeaffine.gonsole.internal.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;


public class WorkspaceScopePreferencesInitializer extends AbstractPreferenceInitializer {

  @Override
  public void initializeDefaultPreferences() {
    new WorkspaceScopePreferences().initializeDefaults();
  }
}
