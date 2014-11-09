package com.codeaffine.gonsole.internal;

import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.assertThat;
import static com.codeaffine.gonsole.pdetest.RegistryHelper.readExtenstionByAttribute;

import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.gonsole.internal.repository.PreferenceRepositoryProvider;


public class PreferenceRepositoryProviderExtensionPDETest {

  private static final String EP_REPOSITORY_PROVIDERS = "com.codeaffine.gonsole.repositoryProviders";
  private static final String CLASS_NAME = PreferenceRepositoryProvider.class.getName();

  @Test
  public void testExtensionExists() {
    Extension actual = readExtenstionByAttribute( EP_REPOSITORY_PROVIDERS, "class", CLASS_NAME );

    assertThat( actual ).isInstantiable( PreferenceRepositoryProvider.class );
  }
}
