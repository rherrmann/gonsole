package com.codeaffine.gonsole.internal;

import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.pgm.TextBuiltin;

import com.google.common.base.Throwables;


public class CommandAccessor {

  private final Object command;
  private final Class<?> type;

  public CommandAccessor( CommandInfo commandInfo ) {
    this( commandInfo.getCommand(), TextBuiltin.class );
  }

  public CommandAccessor( Object command, Class<?> type ) {
    this.command = command;
    this.type = type;
  }

  public void init( Repository repository, OutputStream outputStream ) {
    assignOutputStream( outputStream );
    init( repository );
  }

  public void flush() {
    try {
      for( Field field : type.getDeclaredFields() ) {
        if( "outw".equals( field.getName() ) || "errw".equals( field.getName() ) ) {
          field.setAccessible( true );
          Writer writer = ( Writer )field.get( command );
          writer.flush();
        }
      }
    } catch( Exception exception ) {
      Throwables.propagate( exception );
    }
  }

  private void init( Repository repository ) {
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

  private void assignOutputStream( OutputStream outputStream ) {
    for( Field field : type.getDeclaredFields() ) {
      if( "outs".equals( field.getName() ) || "errs".equals( field.getName() ) ) {
        field.setAccessible( true );
        try {
          field.set( command, outputStream );
        } catch( IllegalAccessException e ) {
          Throwables.propagate( e );
        }
      }
    }
  }
}