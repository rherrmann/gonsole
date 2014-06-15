package com.codeaffine.gonsole.internal;

import static com.codeaffine.gonsole.internal.activator.IconRegistry.GONSOLE;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.google.common.base.Charsets;

public class GitConsolePDETest {

  private CompositeRepositoryProvider repositoryProvider;
  private GitConsole console;

  @Test
  public void testGetEncoding() {
    String encoding = console.getEncoding();

    assertThat( encoding ).isEqualTo( Charsets.UTF_8.name() );
  }

  @Test
  public void testGetImageDescriptor() {
    ImageDescriptor imageDescriptor = console.getImageDescriptor();

    assertThat( imageDescriptor ).isEqualTo( new IconRegistry().getDescriptor( GONSOLE ) );
  }

  @Test
  public void testGetType() {
    String type = console.getType();

    assertThat( type ).isNotEmpty();
  }

  @Test
  public void testGetBackground() {
    console.initialize();

    Color background = console.getBackground();

    assertThat( background ).isNull();
  }

  @Before
  public void setUp() {
    repositoryProvider = new CompositeRepositoryProvider();
    console = new GitConsole( repositoryProvider );
  }

  @After
  public void tearDown() {
    console.destroy();
  }
}
