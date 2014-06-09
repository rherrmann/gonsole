package com.codeaffine.gonsole.internal.activator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.eclipse.jface.resource.ImageRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;

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

  @Test( expected = IllegalArgumentException.class )
  public void testConstructorWithNullAsPluginInstance() {
    new IconRegistrar( null, new ImageRegistry() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testConstructorWithInactivePluginInstance() {
    new IconRegistrar( mock( GonsolePlugin.class ), null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testConstructorWithNullAsRegistry() {
    new IconRegistrar( GonsolePlugin.getInstance(), null );
  }
}