package com.codeaffine.console.core.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.pdetest.console.TestConsoleDefinition;

public class IoStreamProviderPDETest {

  private IoStreamProvider ioStreamProvider;
  private Color blue;

  @Test
  public void testNewOutputStream() {
    IOConsoleOutputStream actual = ( IOConsoleOutputStream )ioStreamProvider.newOutputStream( blue );

    assertThat( actual.getColor() ).isEqualTo( blue );
  }

  @Test
  public void testNewOutputStreamTwice() {
    IOConsoleOutputStream first = ( IOConsoleOutputStream )ioStreamProvider.newOutputStream( blue );
    IOConsoleOutputStream second = ( IOConsoleOutputStream )ioStreamProvider.newOutputStream( blue );

    assertThat( first ).isNotSameAs( second );
  }

  @Test
  public void testGetInputStream() {
    IOConsoleInputStream actual = ( IOConsoleInputStream )ioStreamProvider.getInputStream( blue );

    assertThat( actual.getColor() ).isEqualTo( blue );
  }

  @Test
  public void testGetInputStreamTwice() {
    IOConsoleInputStream first = ( IOConsoleInputStream )ioStreamProvider.getInputStream( blue );
    IOConsoleInputStream second = ( IOConsoleInputStream )ioStreamProvider.getInputStream( blue );

    assertThat( first ).isSameAs( second );
  }

  @Before
  public void setUp() {
    Console console = new Console( new TestConsoleDefinition() );
    ioStreamProvider = new IoStreamProvider( console );
    blue = Display.getDefault().getSystemColor( SWT.COLOR_BLUE );
  }
}