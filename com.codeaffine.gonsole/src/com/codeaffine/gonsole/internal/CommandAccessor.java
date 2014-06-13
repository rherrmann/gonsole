package com.codeaffine.gonsole.internal;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.pgm.Die;
import org.eclipse.jgit.pgm.TextBuiltin;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;


public class CommandAccessor {

  private final Object command;
  private final Class<?> type;
  final ByteArrayOutputStream errorStream;

  public CommandAccessor( CommandInfo commandInfo ) {
    this( commandInfo.getCommand(), TextBuiltin.class, new ByteArrayOutputStream() );
  }

  CommandAccessor( Object command, Class<?> type, ByteArrayOutputStream errorStream ) {
    this.command = command;
    this.type = type;
    this.errorStream = errorStream;
  }

  public void init( Repository repository, OutputStream outputStream ) {
    assignStream( "outs", outputStream );
    assignStream( "errs", errorStream );
    init( repository );
  }

  public String execute( CommandInfo commandInfo ) {
    try {
      commandInfo.getCommand().execute( commandInfo.getArguments() );
    } catch( Die ignore ) {
    } catch( Exception e ) {
      Throwables.propagate( e );
    } finally {
      flush();
    }
    return readErrorStream();
  }

  private String readErrorStream() {
    byte[] bytes = errorStream.toByteArray();
    return bytes.length == 0 ? null : new String( bytes, Charsets.UTF_8 );
  }

  void flush() {
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

  private void assignStream( String fieldName, OutputStream outputStream ) {
    for( Field field : type.getDeclaredFields() ) {
      if( fieldName.equals( field.getName() ) ) {
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