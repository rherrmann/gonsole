package com.codeaffine.gonsole.internal.activator;

import static com.google.common.base.Preconditions.checkArgument;

import org.eclipse.jface.resource.ImageDescriptor;

public class IconRegistry {

  public static final String GONSOLE = "icons/etool16/gonsole.png";

  private final GonsolePlugin pluginInstance;

  public IconRegistry() {
    this( GonsolePlugin.getInstance() );
  }

  IconRegistry( GonsolePlugin pluginInstance ) {
    checkArgument( pluginInstance != null, "Parameter 'pluginInstance' must not be null." );
    checkArgument( pluginInstance.isActive(), "Parameter 'pluginInstance' must be active." );

    this.pluginInstance = pluginInstance;
  }

  public ImageDescriptor getDescriptor( String name ) {
    return pluginInstance.getImageDescriptor( name );
  }
}