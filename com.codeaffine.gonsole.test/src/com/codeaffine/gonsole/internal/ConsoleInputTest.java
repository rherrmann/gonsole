package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;

import org.junit.Before;
import org.junit.Test;

public class ConsoleInputTest {

  private ConsoleIOProvider consoleIOProvider;
  private ConsoleInput consoleInput;

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

  @Before
  public void setUp() {
    consoleIOProvider = mock( ConsoleIOProvider.class );
    consoleInput = new ConsoleInput( consoleIOProvider );
  }

  private void equipConsoleIOProviderWithInput( byte[] bytes ) {
    when( consoleIOProvider.getInputStream() ).thenReturn( new ByteArrayInputStream( bytes ) );
  }
}
