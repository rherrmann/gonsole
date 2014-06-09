package com.codeaffine.gonsole.internal.activator;

import static com.google.common.collect.Lists.newLinkedList;

import java.lang.reflect.Field;
import java.util.Collection;

import com.codeaffine.gonsole.internal.activator.IconRegistry;

public class IconPathProvider {

  public static Object[] provideIconPaths() throws IllegalAccessException {
    Collection<String> result = newLinkedList();
    for( Field field : IconRegistry.class.getFields() ) {
      result.add( ( String )field.get( null ) );
    }
    return result.toArray();
  }
}