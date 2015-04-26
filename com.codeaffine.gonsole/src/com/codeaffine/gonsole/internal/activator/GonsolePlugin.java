package com.codeaffine.gonsole.internal.activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jgit.pgm.CommandCatalog;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
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
    // [rh] workaround for bug 465498: CommandCatalog cannot be used in OSGi
    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=465498
    ClassLoader bufferedContextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader( CommandCatalog.class.getClassLoader() );
      CommandCatalog.all();
    } finally {
      Thread.currentThread().setContextClassLoader( bufferedContextClassLoader );
    }
    // end workaround
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    super.stop( context );
    instance = null;
  }

  protected boolean isActive() {
    return instance != null && instance.getBundle().getState() == Bundle.ACTIVE;
  }

  @Override
  protected void initializeImageRegistry( ImageRegistry registry ) {
    new IconRegistrar( this, registry ).initialize();
  }

  protected ImageDescriptor newImageDescriptor( String imageName ) {
    return imageDescriptorFromPlugin( getBundle().getSymbolicName(), imageName );
  }

  protected ImageDescriptor getImageDescriptor( String name ) {
    return getImageRegistry().getDescriptor( name );
  }
}