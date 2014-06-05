package com.codeaffine.gonsole.internal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


public class GonsolePlugin extends AbstractUIPlugin {

  private static GonsolePlugin instance;

  public static GonsolePlugin getInstance() {
    return instance;
  }

  @Override
  public void start( BundleContext context ) throws Exception {
    super.start( context );
    instance = this;
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    super.stop( context );
  }

  @Override
  protected void initializeImageRegistry( ImageRegistry registry ) {
    Icons.registerImages( registry );
  }

  protected ImageDescriptor newImageDescriptor( String imageName ) {
    return imageDescriptorFromPlugin( getBundle().getSymbolicName(), imageName );
  }

}
