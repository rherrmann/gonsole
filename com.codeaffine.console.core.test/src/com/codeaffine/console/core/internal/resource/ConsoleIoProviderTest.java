package com.codeaffine.console.core.internal.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.internal.IoStreamProvider;
import com.codeaffine.test.util.swt.DisplayHelper;

public class ConsoleIoProviderTest {

  private static final int COLOR_INPUT = SWT.COLOR_BLUE;
  private static final int COLOR_OUTPUT = SWT.COLOR_BLACK;
  private static final int COLOR_PROMPT = SWT.COLOR_GRAY;
  private static final int COLOR_ERROR = SWT.COLOR_RED;

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ConsoleIoProvider consoleIoProvider;
  private InputStream inputStream;
  private OutputStream outputStream;
  private OutputStream promptStream;
  private OutputStream errorStream;

  @Test
  public void testGetInputStream() {
    InputStream actual = consoleIoProvider.getInputStream();

    assertThat( actual ).isSameAs( inputStream );
  }

  @Test
  public void testGetOutputStream() {
    OutputStream actual = consoleIoProvider.getOutputStream();

    assertThat( actual ).isSameAs( outputStream );
  }

  @Test
  public void testGetErrorStream() {
    OutputStream actual = consoleIoProvider.getErrorStream();

    assertThat( actual ).isSameAs( errorStream );
  }

  @Test
  public void testGetPromptStream() {
    OutputStream actual = consoleIoProvider.getPromptStream();

    assertThat( actual ).isSameAs( promptStream );
  }

  @Before
  public void setUp() {
    inputStream = new ByteArrayInputStream( new byte[ 0 ] );
    outputStream = new ByteArrayOutputStream();
    promptStream = new ByteArrayOutputStream();
    errorStream = new ByteArrayOutputStream();
    ColorDefinition colorDefinition = stubColorDefinition();
    IoStreamProvider IoStreamProvider = stubIoProvider( inputStream, outputStream, promptStream, errorStream );
    consoleIoProvider = new ConsoleIoProvider( colorDefinition, IoStreamProvider );
  }

  private ColorDefinition stubColorDefinition() {
    ColorDefinition colorDefinition = mock( ColorDefinition.class );
    when( colorDefinition.getInputColor() ).thenReturn( getColor( COLOR_INPUT ) );
    when( colorDefinition.getOutputColor() ).thenReturn( getColor( COLOR_OUTPUT ) );
    when( colorDefinition.getErrorColor() ).thenReturn( getColor( COLOR_ERROR ) );
    when( colorDefinition.getPromptColor() ).thenReturn( getColor( COLOR_PROMPT ) );
    return colorDefinition;
  }

  private IoStreamProvider stubIoProvider( InputStream in , OutputStream out, OutputStream prompt, OutputStream err ) {
    IoStreamProvider result = mock( IoStreamProvider.class );
    when( result.getInputStream( getColor( COLOR_INPUT ) ) ).thenReturn( in );
    when( result.newOutputStream( getColor( COLOR_OUTPUT ) ) ).thenReturn( out );
    when( result.newOutputStream( getColor( COLOR_PROMPT ) ) ).thenReturn( prompt );
    when( result.newOutputStream( getColor( COLOR_ERROR ) ) ).thenReturn( err );
    return result;
  }

  private Color getColor( int colorCode ) {
    return displayHelper.getDisplay().getSystemColor( colorCode );
  }
}