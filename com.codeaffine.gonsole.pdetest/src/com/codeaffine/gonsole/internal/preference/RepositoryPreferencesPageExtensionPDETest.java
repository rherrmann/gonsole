package com.codeaffine.gonsole.internal.preference;

import static com.codeaffine.test.util.registry.RegistryHelper.findByAttribute;
import static com.codeaffine.test.util.registry.RegistryHelper.getConfigurationElements;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.junit.Test;

import com.codeaffine.gonsole.internal.preference.ConsolePreferencePage;
import com.codeaffine.gonsole.internal.preference.RepositoryPreferencePage;

public class RepositoryPreferencesPageExtensionPDETest {

  private static final String EP_PREFERENCE_PAGES = "org.eclipse.ui.preferencePages";

  @Test
  public void testClassIsInstantiable() throws Exception {
    IConfigurationElement element = getRepositoryPreferencesPageConfigurationElement();

    Object instance = element.createExecutableExtension( "class" );

    assertThat( instance ).isInstanceOf( RepositoryPreferencePage.class );
  }

  @Test
  public void testClassImplementsIWorkbenchPreferencePage() throws CoreException {
    IConfigurationElement element = getRepositoryPreferencesPageConfigurationElement();

    Object instance = element.createExecutableExtension( "class" );

    assertThat( instance ).isInstanceOf( IWorkbenchPreferencePage.class );
  }

  @Test
  public void testCategory() {
    IConfigurationElement element = getRepositoryPreferencesPageConfigurationElement();

    String category = element.getAttribute( "category" );

    assertThat( category ).isEqualTo( ConsolePreferencePage.ID );
  }

  @Test
  public void testName() {
    IConfigurationElement element = getRepositoryPreferencesPageConfigurationElement();

    assertThat( element.getAttribute( "name" ) ).isNotEmpty();
  }

  private static IConfigurationElement getRepositoryPreferencesPageConfigurationElement() {
    IConfigurationElement[] elements = getConfigurationElements( EP_PREFERENCE_PAGES );
    return findByAttribute( elements, "id", RepositoryPreferencePage.ID );
  }
}
