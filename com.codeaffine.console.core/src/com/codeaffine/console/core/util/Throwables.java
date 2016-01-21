package com.codeaffine.console.core.util;

import static java.util.Objects.requireNonNull;

import java.io.PrintWriter;
import java.io.StringWriter;

/*
 * Adapted from com.google.common.base.Throwables
 */
public class Throwables {

  public static String getStackTraceAsString( Throwable throwable ) {
    StringWriter stringWriter = new StringWriter();
    throwable.printStackTrace( new PrintWriter( stringWriter ) );
    return stringWriter.toString();
  }

  public static void propagate( Throwable throwable ) {
    requireNonNull( throwable, "throwable" );
    propagateIfInstanceOf( throwable, Error.class );
    propagateIfInstanceOf( throwable, RuntimeException.class );
    throw new RuntimeException( throwable );
  }

  private static <T extends Throwable> void propagateIfInstanceOf( Throwable throwable,
                                                                   Class<T> declaredType )
    throws T
  {
    if( declaredType.isInstance( throwable ) ) {
      throw declaredType.cast( throwable );
    }
  }

  public Throwables() { }
}
