package com.codeaffine.gonsole.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class GonsolePlugin extends AbstractUIPlugin {

  public static final String PLUGIN_ID = "com.codeaffine.gonsole";
  private static GonsolePlugin plugin;

  public static GonsolePlugin getDefault() {
    return plugin;
  }

  @Override
  public void start( BundleContext context ) throws Exception {
    super.start( context );
    plugin = this;
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    plugin = null;
    super.stop( context );
  }

}
