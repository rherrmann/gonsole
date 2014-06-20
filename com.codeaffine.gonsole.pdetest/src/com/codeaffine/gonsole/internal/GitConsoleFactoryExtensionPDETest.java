package com.codeaffine.gonsole.internal;

import static com.codeaffine.test.util.registry.RegistryHelper.findByAttribute;
import static com.codeaffine.test.util.registry.RegistryHelper.getConfigurationElements;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.junit.Before;
import org.junit.Test;


public class GitConsoleFactoryExtensionPDETest {

  private static final String EP_CONSOLE_FACTORIES = "org.eclipse.ui.console.consoleFactories";
  private static final String FACTORY_CLASS_NAME = GitConsoleFactory.class.getName();

  private IConfigurationElement configElement;

  @Test
  public void testClassIsInstantiable() throws CoreException {
    Object instance = configElement.createExecutableExtension( "class" );

    assertThat( instance ).isNotNull();
  }

  @Test
  public void testExtension() {
    assertThat( configElement.getAttribute( "icon" ) ).isEqualTo( "icons/etool16/gonsole.png" );
    assertThat( configElement.getAttribute( "label" ) ).isNotEmpty();
  }

  @Before
  public void setUp() {
    IConfigurationElement[] elements = getConfigurationElements( EP_CONSOLE_FACTORIES );
    configElement = findByAttribute( elements, "class", FACTORY_CLASS_NAME );
  }
}
