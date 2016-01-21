package com.codeaffine.console.core.util;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.codeaffine.console.core.util.Throwables;

public class ThrowablesTest {

  @Test
  public void testPropagateWithNullArgument() {
    Throwable throwable = thrownBy( () -> Throwables.propagate( null ) );

    assertThat( throwable ).isInstanceOf( NullPointerException.class );
  }

  @Test
  public void testPropagateError() {
    Error error = new Error();

    Throwable throwable = thrownBy( () -> Throwables.propagate( error ) );

    assertThat( throwable ).isSameAs( error );
  }

  @Test
  public void testPropagateErrorSubclass() {
    Error error = new AssertionError();

    Throwable throwable = thrownBy( () -> Throwables.propagate( error ) );

    assertThat( throwable ).isSameAs( error );
  }

  @Test
  public void testPropagateRuntimeException() {
    RuntimeException runtimeException = new RuntimeException();

    Throwable throwable = thrownBy( () -> Throwables.propagate( runtimeException ) );

    assertThat( throwable ).isSameAs( runtimeException );
  }

  @Test
  public void testPropagateRuntimeExceptionSubclass() {
    RuntimeException runtimeException = new IllegalArgumentException();

    Throwable throwable = thrownBy( () -> Throwables.propagate( runtimeException ) );

    assertThat( throwable ).isSameAs( runtimeException );
  }

  @Test
  public void testPropagateCheckedException() {
    Exception exception = new Exception();

    Throwable throwable = thrownBy( () -> Throwables.propagate( exception ) );

    assertThat( throwable ).isInstanceOf( RuntimeException.class );
    assertThat( throwable.getCause() ).isSameAs( exception );
  }

  @Test
  public void testPropagateCheckedExceptionSubclass() {
    Exception exception = new IOException();

    Throwable throwable = thrownBy( () -> Throwables.propagate( exception ) );

    assertThat( throwable ).isInstanceOf( RuntimeException.class );
    assertThat( throwable.getCause() ).isSameAs( exception );
  }
}
