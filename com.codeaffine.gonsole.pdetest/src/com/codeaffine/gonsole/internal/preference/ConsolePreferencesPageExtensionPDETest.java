package com.codeaffine.gonsole.internal.preference;

import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.assertThat;
import static com.codeaffine.gonsole.pdetest.RegistryHelper.readExtenstionByAttribute;

import org.eclipse.ui.IWorkbenchPreferencePage;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;

public class ConsolePreferencesPageExtensionPDETest {

  private static final String EP_PREFERENCE_PAGES = "org.eclipse.ui.preferencePages";

  @Test
  public void testExtension() {
    Extension actual = readExtenstionByAttribute( EP_PREFERENCE_PAGES, "id", ConsolePreferencePage.ID );

    assertThat( actual )
      .isInstantiable( ConsolePreferencePage.class )
      .isInstantiable( IWorkbenchPreferencePage.class )
      .hasAttributeValue( "category", "org.eclipse.team.ui.TeamPreferences" )
      .hasNonEmptyAttributeValueFor( "name" );
  }
}