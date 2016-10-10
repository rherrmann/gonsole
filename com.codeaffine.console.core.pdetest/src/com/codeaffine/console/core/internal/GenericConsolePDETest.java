package com.codeaffine.console.core.internal;

import static com.codeaffine.console.core.ConsoleConstants.ENCODING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.pdetest.console.TestConsoleComponentFactory;
import com.codeaffine.console.core.pdetest.console.TestConsoleConfigurer;

public class GenericConsolePDETest {

  private TestConsoleConfigurer consoleConfigurer;
  private GenericConsole console;

  @Test
  public void testInitialize() {
    console.initialize();

    verify( consoleConfigurer ).configure( console );
  }

  @Test
  public void testGetConsoleComponentFactory() {
    // should return null without initialization
    assertThat( console.getConsoleComponentFactory() ).isNull();

    // set a console
    console.setConsoleComponentFactory( new TestConsoleComponentFactory() );

    // note, the implementation wraps it into a GenericConsoleComponentFactory
    ConsoleComponentFactory componentFactory = console.getConsoleComponentFactory();
    assertThat( componentFactory ).isNotNull();
    assertThat( componentFactory.getClass() ).isSameAs( GenericConsoleComponentFactory.class );
  }

  @Test
  public void testGetEncoding() {
    String encoding = console.getEncoding();

    assertThat( encoding ).isEqualTo( ENCODING.name() );
  }

  @Test
  public void testGetImageDescriptor() {
    console.initialize();

    ImageDescriptor imageDescriptor = console.getImageDescriptor();

    assertThat( imageDescriptor ).isEqualTo( TestConsoleConfigurer.IMAGE );
  }

  @Test
  public void testGetName() {
    console.initialize();

    String name = console.getName();

    assertThat( name ).isEqualTo( TestConsoleConfigurer.TITLE );
  }

  @Test
  public void testGetType() {
    String type = console.getType();

    assertThat( type ).isSameAs( consoleConfigurer.getClass().getName() );
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
    consoleConfigurer = spy( new TestConsoleConfigurer() );
    console = new GenericConsole( consoleConfigurer );
  }

  @After
  public void tearDown() {
    console.destroy();
  }
}