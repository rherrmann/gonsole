package com.codeaffine.gonsole.test.io;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;


public class Streams {

  public static void close( Closeable closable ) {
    try {
      closable.close();
    } catch( IOException ioe ) {
      throw new RuntimeException( ioe );
    }
  }

  public static InputStream toUtf8Stream( String string ) {
    return new ByteArrayInputStream( toUtf8Bytes( string ) );
  }

  public static byte[] toUtf8Bytes( String string ) {
    return string.getBytes( UTF_8 );
  }

  private Streams() {
    // prevent instance creation
  }
}
