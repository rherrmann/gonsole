package com.codeaffine.gonsole.pdetest;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.RegistryAdapter;

public class RegistryHelper {

  public static Extension readExtenstionByAttribute(
    String extensionPointId, String attributeName, String attributeValue )
  {
    return new RegistryAdapter()
      .readExtension( extensionPointId )
      .thatMatches( attribute( attributeName, attributeValue ) )
      .process();
  }
}