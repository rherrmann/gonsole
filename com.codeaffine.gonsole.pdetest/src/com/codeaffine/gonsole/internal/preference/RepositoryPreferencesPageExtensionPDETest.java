package com.codeaffine.gonsole.internal.preference;

import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.assertThat;
import static com.codeaffine.gonsole.pdetest.RegistryHelper.readExtenstionByAttribute;

import org.eclipse.ui.IWorkbenchPreferencePage;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;

public class RepositoryPreferencesPageExtensionPDETest {

  private static final String EP_PREFERENCE_PAGES = "org.eclipse.ui.preferencePages";

  @Test
  public void testExtension() {
    Extension actual = readExtenstionByAttribute( EP_PREFERENCE_PAGES, "id", RepositoryPreferencePage.ID );

    assertThat( actual )
      .isInstantiable( RepositoryPreferencePage.class )
      .isInstantiable( IWorkbenchPreferencePage.class )
      .hasAttributeValue( "category", ConsolePreferencePage.ID )
      .hasNonEmptyAttributeValueFor( "name" );
  }
}
