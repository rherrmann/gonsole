package com.codeaffine.test.util.registry;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class RegistryHelper {

  public static IConfigurationElement[] getConfigurationElements( String extensionPointId ) {
    return Platform.getExtensionRegistry().getConfigurationElementsFor( extensionPointId );
  }

  public static IConfigurationElement findByAttribute( IConfigurationElement[] elements,
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

  private RegistryHelper() {
    // prevent instantiation
  }
}
