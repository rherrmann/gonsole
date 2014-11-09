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
package com.codeaffine.gonsole.test.util.osgi.testsuite;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.runners.model.InitializationError;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;


class TestCollector {
  private final String[] bundleSymbolicNames;
  private final BundleContext bundleContext;
  private final Set<Class<?>> classes;
  private final Properties devProperties;

  TestCollector( String... bundleSymbolicNames ) {
    this( getBundleContext(), bundleSymbolicNames );
  }

  TestCollector( BundleContext bundleContext, String... bundleSymbolicNames ) {
    this.bundleContext = bundleContext;
    this.bundleSymbolicNames = bundleSymbolicNames;
    this.classes = new HashSet<Class<?>>();
    this.devProperties = new DevPropertiesLoader( bundleContext ).load();
  }

  Class<?>[] collect() throws InitializationError {
    for( String bundleSymbolicName : bundleSymbolicNames ) {
      collect( bundleSymbolicName );
    }
    return classes.toArray( new Class[ classes.size() ] );
  }

  private void collect( String bundleSymbolicName ) throws InitializationError {
    Bundle bundle = getBundle( bundleSymbolicName );
    Class<?>[] scan = new ClassPathScanner( bundle, devProperties, "*PDETest.class" ).scan();
    classes.addAll( Arrays.asList( scan ) );
  }

  private Bundle getBundle( String bundleSymbolicName ) throws InitializationError {
    Bundle result = null;
    Bundle[] bundles = bundleContext.getBundles();
    for( int i = 0; result == null && i < bundles.length; i++ ) {
      if( bundles[ i ].getSymbolicName().equals( bundleSymbolicName ) ) {
        result = bundles[ i ];
      }
    }
    if( result == null ) {
      throw new InitializationError( "Bundle not found: " + bundleSymbolicName );
    }
    return result;
  }

  private static BundleContext getBundleContext() {
    Bundle bundle = FrameworkUtil.getBundle( TestCollector.class );
    return bundle == null ? null : bundle.getBundleContext();
  }
}