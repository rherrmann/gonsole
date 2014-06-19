package com.codeaffine.gonsole.internal.gitconsole.preference;

import static org.junit.Assert.assertNotNull;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.junit.Test;

import com.codeaffine.test.util.registry.RegistryHelper;


public class WorkspaceScopePreferencesInitializerPDETest {

  private static final String EP_PREFERENCES = "org.eclipse.core.runtime.preferences";

  @Test
  public void testExtension() throws CoreException {
    IConfigurationElement configurationElement = getConfigurationElement();

    Object instance = configurationElement.createExecutableExtension( "class" );

    assertNotNull( instance );
  }

  private static IConfigurationElement getConfigurationElement() {
    IConfigurationElement[] elements = RegistryHelper.getConfigurationElements( EP_PREFERENCES );
    String className = WorkspaceScopePreferencesInitializer.class.getName();
    return RegistryHelper.findByAttribute( elements, "class", className );
  }
}

