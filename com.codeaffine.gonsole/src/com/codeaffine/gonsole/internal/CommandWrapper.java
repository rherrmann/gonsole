package com.codeaffine.gonsole.internal;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.pgm.TextBuiltin;

import com.google.common.base.Throwables;


public class CommandWrapper {

  private final Object command;
  private final Class<?> type;

  public CommandWrapper( Object command ) {
    this( command, TextBuiltin.class );
  }

  public CommandWrapper( Object command, Class<?> type ) {
    this.command = command;
    this.type = type;
  }

  public void init( Repository repository, OutputStream outputStream ) {
    Field[] fields = type.getDeclaredFields();
    for( Field field : fields ) {
      if( "outs".equals( field.getName() ) || "errs".equals( field.getName() ) ) {
        field.setAccessible( true );
        try {
          field.set( command, outputStream );
        } catch( IllegalAccessException e ) {
          Throwables.propagate( e );
        }
      }
    }

    try {
      Method initMethod = type.getDeclaredMethod( "init", Repository.class, String.class );
      initMethod.setAccessible( true );
      initMethod.invoke( command, repository, null );
    } catch( InvocationTargetException exception ) {
      Throwables.propagate( exception.getCause() );
    } catch( Exception exception ) {
      Throwables.propagate( exception );
    }
  }

}
