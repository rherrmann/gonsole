package com.codeaffine.console.core.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.console.TestConsoleDefinition;

public class ConsolePDETest {

  private TestConsoleDefinition definition;
  private Console console;

  @Test
  public void testGetEncoding() {
    String encoding = console.getEncoding();

    assertThat( encoding ).isEqualTo( definition.getEncoding().name() );
  }

  @Test
  public void testGetImageDescriptor() {
    ImageDescriptor imageDescriptor = console.getImageDescriptor();

    assertThat( imageDescriptor ).isEqualTo( definition.getImage() );
  }

  @Test
  public void testGetType() {
    String type = console.getType();

    assertThat( type ).isSameAs( definition.getType() );
  }

  @Test
  public void testGetBackground() {
    console.initialize();

    Color background = console.getBackground();

    assertThat( background ).isNull();
  }

  @Test
  public void testDestroyingConsoleClosesStreams() {
    IoStreamProvider ioStreamProvider = new IoStreamProvider( console );
    IOConsoleOutputStream outputStream = ( IOConsoleOutputStream )ioStreamProvider.newOutputStream( null );

    console.destroy();

    assertThat( outputStream.isClosed() ).isTrue();
  }

  @Before
  public void setUp() {
    definition = new TestConsoleDefinition();
    console = new Console( definition );
  }

  @After
  public void tearDown() {
    console.destroy();
  }
}