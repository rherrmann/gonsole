package com.codeaffine.gonsole.internal.preference;

import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.assertThat;
import static com.codeaffine.gonsole.pdetest.RegistryHelper.readExtenstionByAttribute;

import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;


public class WorkspaceScopePreferencesInitializerPDETest {

  private static final String CLASS_NAME = WorkspaceScopePreferencesInitializer.class.getName();
  private static final String EP_PREFERENCES = "org.eclipse.core.runtime.preferences";

  @Test
  public void testExtension() {
    Extension actual = readExtenstionByAttribute( EP_PREFERENCES, "class", CLASS_NAME );

    assertThat( actual ).isInstantiable( WorkspaceScopePreferencesInitializer.class );
  }
}