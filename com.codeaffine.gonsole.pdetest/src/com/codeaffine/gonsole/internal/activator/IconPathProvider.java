package com.codeaffine.gonsole.internal.activator;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;

public class IconPathProvider {

  public static Object[] provideIconPaths() throws IllegalAccessException {
    Collection<String> result = new LinkedList<>();
    for( Field field : IconRegistry.class.getFields() ) {
      result.add( ( String )field.get( null ) );
    }
    return result.toArray();
  }
}