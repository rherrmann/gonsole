package com.codeaffine.test.io;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Charsets;


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
    return string.getBytes( Charsets.UTF_8 );
  }

  private Streams() {
    // prevent instance creation
  }
}
