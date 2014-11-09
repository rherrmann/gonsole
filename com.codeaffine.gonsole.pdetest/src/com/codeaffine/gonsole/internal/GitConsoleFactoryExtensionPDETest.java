package com.codeaffine.gonsole.internal;

import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.assertThat;
import static com.codeaffine.gonsole.pdetest.RegistryHelper.readExtenstionByAttribute;

import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.gonsole.GitConsoleFactory;

public class GitConsoleFactoryExtensionPDETest {

  private static final String EP_CONSOLE_FACTORIES = "org.eclipse.ui.console.consoleFactories";
  private static final String FACTORY_CLASS_NAME = GitConsoleFactory.class.getName();

  @Test
  public void testExtension() {
    Extension actual = readExtenstionByAttribute( EP_CONSOLE_FACTORIES, "class", FACTORY_CLASS_NAME );

    assertThat( actual )
      .hasAttributeValue( "icon", "icons/etool16/gonsole.png" )
      .hasNonEmptyAttributeValueFor( "label" )
      .isInstantiable( GitConsoleFactory.class );
  }
}