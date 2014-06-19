package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.internal.resource.ConsoleIoProvider;

public class InputTest {

  private ConsoleIoProvider consoleIoProvider;
  private Input consoleInput;

  @Test
  public void testReadLine() {
    equipConsoleIOProviderWithInput( "foo\n".getBytes() );

    String read = consoleInput.readLine();

    assertThat( read ).isEqualTo( "foo" );
  }

  @Test
  public void testReadLineAfterEOF() {
    equipConsoleIOProviderWithInput( new byte[ 0 ] );

    String read = consoleInput.readLine();

    assertThat( read ).isNull();
  }

  @Test
  public void testReadWithIOException() throws IOException {
    InputStream inStream = createInputStreamSpy();
    equipInputStreamWithIOException( inStream );
    equipConsoleIOProviderWithIputStream( inStream );

    String read = consoleInput.readLine();

    assertThat( read ).isNull();
  }

  @Before
  public void setUp() {
    consoleIoProvider = mock( ConsoleIoProvider.class );
    consoleInput = new Input( consoleIoProvider );
  }

  private void equipConsoleIOProviderWithInput( byte[] bytes ) {
    equipConsoleIOProviderWithIputStream( new ByteArrayInputStream( bytes ) );
  }

  private void equipConsoleIOProviderWithIputStream( InputStream result ) {
    when( consoleIoProvider.getInputStream() ).thenReturn( result );
  }

  private static void equipInputStreamWithIOException( InputStream inStream ) throws IOException {
    doThrow( new IOException() ).when( inStream ).read( any( byte[].class ), anyInt(), anyInt() );
  }

  private static InputStream createInputStreamSpy() {
    InputStream inputStream = new ByteArrayInputStream( "foo\n".getBytes() );
    return spy( new BufferedInputStream( inputStream ) );
  }
}