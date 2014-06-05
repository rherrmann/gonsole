package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
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
    IConfigurationElement[] elements = getConsoleFactoryExtensions();
    configElement = findByAttribute( elements, "class", FACTORY_CLASS_NAME );
  }

  private static IConfigurationElement[] getConsoleFactoryExtensions() {
    return Platform.getExtensionRegistry().getConfigurationElementsFor( EP_CONSOLE_FACTORIES );
  }

  private static IConfigurationElement findByAttribute( IConfigurationElement[] elements,
                                                        String attributeName,
                                                        String attributeValue )
  {
    IConfigurationElement result = null;
    for( int i = 0; result == null && i < elements.length; i++ ) {
      if( attributeValue.equals( elements[ i ].getAttribute( attributeName ) ) ) {
        result = elements[ i ];
      }
    }
    return result;
  }
}
