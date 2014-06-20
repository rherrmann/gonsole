package com.codeaffine.gonsole.internal.interpreter;

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

class CommandAccessor {

  private static final String FATAL_PREFIX = "fatal: ";

  private static final Class<TextBuiltin> COMMAND_TYPE = TextBuiltin.class;

  private final CommandInfo commandInfo;
  private final ByteArrayOutputStream errorStream;

  CommandAccessor( CommandInfo commandInfo ) {
    this( commandInfo, new ByteArrayOutputStream() );
  }

  CommandAccessor( CommandInfo commandInfo, ByteArrayOutputStream errorStream ) {
    this.commandInfo = commandInfo;
    this.errorStream = errorStream;
  }

  void init( Repository repository, OutputStream outputStream ) {
    assignStream( "outs", outputStream );
    assignStream( "errs", errorStream );
    init( repository );
  }

  String execute() {
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
    String result = null;
    byte[] bytes = errorStream.toByteArray();
    if( bytes.length != 0 ) {
      result = new String( bytes, Charsets.UTF_8 ).trim();
      if( result.startsWith( FATAL_PREFIX ) ) {
        result = result.substring( FATAL_PREFIX.length() );
      }
    }
    return result;
  }

  void flush() {
    try {
      for( Field field : COMMAND_TYPE.getDeclaredFields() ) {
        if( "outw".equals( field.getName() ) || "errw".equals( field.getName() ) ) {
          field.setAccessible( true );
          Writer writer = ( Writer )field.get( commandInfo.getCommand() );
          writer.flush();
        }
      }
    } catch( Exception exception ) {
      Throwables.propagate( exception );
    }
  }

  private void init( Repository repository ) {
    try {
      Method initMethod = COMMAND_TYPE.getDeclaredMethod( "init", Repository.class, String.class );
      initMethod.setAccessible( true );
      initMethod.invoke( commandInfo.getCommand(), repository, null );
    } catch( InvocationTargetException exception ) {
      Throwables.propagate( exception.getCause() );
    } catch( Exception exception ) {
      Throwables.propagate( exception );
    }
  }

  private void assignStream( String fieldName, OutputStream outputStream ) {
    for( Field field : COMMAND_TYPE.getDeclaredFields() ) {
      if( fieldName.equals( field.getName() ) ) {
        field.setAccessible( true );
        try {
          field.set( commandInfo.getCommand(), outputStream );
        } catch( IllegalAccessException e ) {
          Throwables.propagate( e );
        }
      }
    }
  }
}