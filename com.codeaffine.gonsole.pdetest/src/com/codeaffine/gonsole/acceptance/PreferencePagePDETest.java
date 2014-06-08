package com.codeaffine.gonsole.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.core.runtime.preferences.InstanceScope.INSTANCE;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.internal.preference.RepositoryPreferencePage;
import com.codeaffine.gonsole.internal.preference.WorkspaceScopePreferences;
import com.codeaffine.test.util.swt.DisplayHelper;

public class PreferencePagePDETest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private RepositoryPreferencePage preferencePage;
  private WorkspaceScopePreferences preferences;

  @Test
  public void testEnterRepositoryLocationsAndPerformOk() {
    preferencePage.createControl( displayHelper.createShell() );
    Text textControl = preferencePage.getRepositoryLocationsField().getTextControl();
    String enteredText = "/path/to/repo1" + textControl.getLineDelimiter() + "/path/to/repo2";
    textControl.setText( enteredText );

    preferencePage.performOk();

    assertThat( preferences.getRepositoryLocations() ).isEqualTo( enteredText );
  }

  @Test
  public void testEnterRepositoryLocationsAndPerformCancel() {
    preferencePage.createControl( displayHelper.createShell() );
    Text textControl = preferencePage.getRepositoryLocationsField().getTextControl();
    textControl.setText( "/path/to/repo" );

    preferencePage.performCancel();

    assertThat( preferences.getRepositoryLocations() ).isEmpty();
  }

  @Before
  public void setUp() {
    preferences = createWorkspaceScopePreferences();
    preferencePage = new RepositoryPreferencePage();
    preferencePage.setPreferenceStore( preferences.getPreferenceStore() );
  }

  @After
  public void tearDown() {
    preferencePage.dispose();
  }

  private static WorkspaceScopePreferences createWorkspaceScopePreferences() {
    String qualifier = "com.codeaffine.gonsole.pdetest-" + System.currentTimeMillis();
    IPreferenceStore preferenceStore = new ScopedPreferenceStore( INSTANCE, qualifier );
    WorkspaceScopePreferences result = new WorkspaceScopePreferences( preferenceStore );
    result.initializeDefaults();
    return result;
  }
}
