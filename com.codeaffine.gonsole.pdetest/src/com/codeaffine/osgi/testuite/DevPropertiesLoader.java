/*******************************************************************************
 * Copyright (c) 2012, 2013 Rüdiger Herrmann.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Rüdiger Herrmann - initial API and implementation
 ******************************************************************************/
package com.codeaffine.osgi.testuite;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.osgi.framework.BundleContext;


class DevPropertiesLoader {
  private final BundleContext bundleContext;
  private final Properties properties;

  DevPropertiesLoader( BundleContext bundleContext ) {
    this.bundleContext = bundleContext;
    this.properties = new Properties();
  }

  Properties load() {
    if( bundleContext != null ) {
      String osgiDevProperty = bundleContext.getProperty( "osgi.dev" );
      if( osgiDevProperty != null && osgiDevProperty.length() > 0 ) {
        URL url = toUrl( osgiDevProperty );
        load( url );
      }
    }
    return properties;
  }

  private void load( URL url ) {
    try {
      InputStream inputStream = url.openStream();
      properties.load( inputStream );
      inputStream.close();
    } catch( IOException ioe ) {
      throw new RuntimeException( ioe );
    }
  }

  private static URL toUrl( String string ) {
    URL result;
    try {
      result = new URL( string );
    } catch( MalformedURLException mue ) {
      result = null;
    }
    return result;
  }

}
