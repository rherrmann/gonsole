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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;

import org.junit.runners.model.InitializationError;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;


class ClassPathScanner {
  private static final String DOT_CLASS = ".class";

  private final Bundle bundle;
  private final Properties devProperties;
  private final String pattern;
  private final Set<Class<?>> classes;

  ClassPathScanner( Bundle bundle, Properties devProperties, String pattern ) {
    this.bundle = bundle;
    this.devProperties = devProperties;
    this.pattern = pattern;
    this.classes = new HashSet<Class<?>>();
  }

  Class<?>[] scan() throws InitializationError {
    Collection<String> resources = listResources();
    loadClasses( resources );
    return classes.toArray( new Class[ classes.size() ] );
  }

  private Collection<String> listResources() {
    Collection<String> result = new ArrayList<String>();
    String[] classPathRoots = getClassPathRoots();
    for( String classPathRoot : classPathRoots ) {
      Collection<String> resources = listResources( classPathRoot );
      result.addAll( resources );
    }
    return result;
  }

  private Collection<String> listResources( String classPathRoot ) {
    BundleWiring bundleWiring = bundle.adapt( BundleWiring.class );
    int options = BundleWiring.LISTRESOURCES_LOCAL | BundleWiring.LISTRESOURCES_RECURSE;
    return bundleWiring.listResources( classPathRoot, pattern, options );
  }

  private void loadClasses( Collection<String> resources ) throws InitializationError {
    for( String resource : resources ) {
      String className = toClassName( stripClassPathRoot( resource ) );
      Class<?> loadedClass = loadClass( className );
      classes.add( loadedClass );
    }
  }

  private String[] getClassPathRoots() {
    Collection<String> classPathRoots = new LinkedList<String>();
    if( !devProperties.isEmpty() ) {
      appendClassPathRoots( classPathRoots, devProperties.getProperty( bundle.getSymbolicName() ) );
      appendClassPathRoots( classPathRoots, devProperties.getProperty( "*" ) );
    }
    if( classPathRoots.isEmpty() ) {
      classPathRoots.add( "/" );
    }
    return classPathRoots.toArray( new String[ classPathRoots.size() ] );
  }

  private Class<?> loadClass( String className ) throws InitializationError {
    try {
      return bundle.loadClass( className );
    } catch( ClassNotFoundException exception ) {
      throw new InitializationError( exception );
    }
  }

  private String stripClassPathRoot( String resource ) {
    String result = resource;
    String[] classPathRoots = getClassPathRoots();
    boolean found = false;
    for( int i = 0; !found && i < classPathRoots.length; i++ ) {
      String classPathRoot = classPathRoots[ i ];
      if( result.startsWith( classPathRoot ) ) {
        result = result.substring( classPathRoot.length() );
        found = true;
      }
    }
    if( result.startsWith( "/" ) ) {
      result = result.substring( 1 );
    }
    return result;
  }

  private static String toClassName( String string ) {
    String result = string.replace( '/', '.' );
    if( result.endsWith( DOT_CLASS ) ) {
      result = result.substring( 0, result.length() - DOT_CLASS.length() );
    }
    return result;
  }

  private static void appendClassPathRoots( Collection<String> collection, String classPathRoots ) {
    if( classPathRoots != null ) {
      collection.addAll( Arrays.asList( classPathRoots.split( "," ) ) );
    }
  }

}
