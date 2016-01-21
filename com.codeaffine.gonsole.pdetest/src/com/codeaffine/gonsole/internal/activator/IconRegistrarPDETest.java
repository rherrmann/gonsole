package com.codeaffine.gonsole.internal.activator;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.resource.ImageRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class IconRegistrarPDETest {

  @Test
  @Parameters( source = IconPathProvider.class )
  public  void testInitialize( String iconPath ) {
    ImageRegistry registry = new ImageRegistry();
    IconRegistrar registrar = new IconRegistrar( GonsolePlugin.getInstance(), registry );

    registrar.initialize();

    assertThat( registry.get( iconPath ) ).isNotNull();
  }
}