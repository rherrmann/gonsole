package com.codeaffine.gonsole.internal;

import static com.codeaffine.test.util.rergistry.RegistryHelper.findByAttribute;
import static com.codeaffine.test.util.rergistry.RegistryHelper.getConfigurationElements;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.junit.Test;

import com.codeaffine.gonsole.internal.repository.PreferenceRepositoryProvider;


public class PreferenceRepositoryProviderExtensionPDETest {

  private static final String EP_REPOSITORY_PROVIDERS
    = "com.codeaffine.gonsole.repositoryProviders";
  private static final String CLASS_NAME
    = PreferenceRepositoryProvider.class.getName();

  @Test
  public void testExtensionExists() {
    IConfigurationElement[] elements = getConfigurationElements( EP_REPOSITORY_PROVIDERS );

    IConfigurationElement element = findByAttribute( elements, "class", CLASS_NAME );

    assertThat( element ).isNotNull();
  }

  @Test
  public void testClassIsInstantiable() throws CoreException {
    IConfigurationElement[] elements = getConfigurationElements( EP_REPOSITORY_PROVIDERS );
    IConfigurationElement element = findByAttribute( elements, "class", CLASS_NAME );

    Object instance = element.createExecutableExtension( "class" );

    assertThat( instance ).isInstanceOf( PreferenceRepositoryProvider.class );
  }

}
