package com.codeaffine.gonsole.internal.activator;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.eclipse.jface.resource.ImageRegistry;

class IconRegistrar {

  private final GonsolePlugin pluginInstance;
  private final ImageRegistry registry;

  IconRegistrar( GonsolePlugin pluginInstance, ImageRegistry registry ) {
    checkArgument( pluginInstance != null, "Parameter 'pluginInstance' must not be null." );
    checkArgument( pluginInstance.isActive(), "Parameter 'pluginInstance' must be active." );
    checkArgument( registry != null, "Parameter 'registry' must not be null." );

    this.registry = registry;
    this.pluginInstance = pluginInstance;
  }

  void initialize() {
    for( Field field : IconRegistry.class.getFields() ) {
      if( isStringConstant( field ) ) {
        registerImage( getStringValue( field ) );
      }
    }
  }

  private static boolean isStringConstant( Field field ) {
    int modifiers = field.getModifiers();
    Class<?> type = field.getType();
    return Modifier.isFinal( modifiers ) && Modifier.isStatic( modifiers ) && type == String.class;
  }

  private static String getStringValue( Field field ) {
    try {
      return ( String )field.get( null );
    } catch( IllegalAccessException iae ) {
      throw new RuntimeException( iae );
    }
  }

  private void registerImage( String imageName ) {
    registry.put( imageName, pluginInstance.newImageDescriptor( imageName ) );
  }
}