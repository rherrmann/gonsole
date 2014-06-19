package com.codeaffine.gonsole.internal.gitconsole.preference;

import static com.codeaffine.test.util.registry.RegistryHelper.findByAttribute;
import static com.codeaffine.test.util.registry.RegistryHelper.getConfigurationElements;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.junit.Test;

import com.codeaffine.gonsole.internal.gitconsole.preference.ConsolePreferencePage;

public class ConsolePreferencesPageExtensionPDETest {

  private static final String EP_PREFERENCE_PAGES = "org.eclipse.ui.preferencePages";

  @Test
  public void testClassIsInstantiable() throws Exception {
    IConfigurationElement element = getConsolePreferencesPageConfigurationElement();

    Object instance = element.createExecutableExtension( "class" );

    assertThat( instance ).isInstanceOf( ConsolePreferencePage.class );
  }

  @Test
  public void testClassImplementsIWorkbenchPreferencePage() throws CoreException {
    IConfigurationElement element = getConsolePreferencesPageConfigurationElement();

    Object instance = element.createExecutableExtension( "class" );

    assertThat( instance ).isInstanceOf( IWorkbenchPreferencePage.class );
  }

  @Test
  public void testCategory() {
    IConfigurationElement element = getConsolePreferencesPageConfigurationElement();

    String category = element.getAttribute( "category" );

    assertThat( category ).isEqualTo( "org.eclipse.team.ui.TeamPreferences" );
  }

  @Test
  public void testName() {
    IConfigurationElement element = getConsolePreferencesPageConfigurationElement();

    assertThat( element.getAttribute( "name" ) ).isNotEmpty();
  }

  private static IConfigurationElement getConsolePreferencesPageConfigurationElement() {
    IConfigurationElement[] elements = getConfigurationElements( EP_PREFERENCE_PAGES );
    return findByAttribute( elements, "id", ConsolePreferencePage.ID );
  }
}
