package com.codeaffine.gonsole.internal.interpreter;

import static com.codeaffine.console.core.ConsoleConstants.ENCODING;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.pgm.Die;
import org.eclipse.jgit.pgm.TextBuiltin;

import com.codeaffine.console.core.util.Throwables;

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
      if( commandInfo.isHelpRequested() ) {
        printHelp();
      } else {
        executeCommand();
      }
    } catch( Die ignore ) {
    } catch( Exception e ) {
      Throwables.propagate( e );
    } finally {
      flush();
    }
    return readErrorStream();
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
    } catch( IOException | IllegalAccessException exception ) {
      throw new RuntimeException( exception );
    }
  }

  private void executeCommand() throws Exception {
    commandInfo.getCommand().execute( commandInfo.getArguments() );
  }

  private void printHelp() throws IOException {
    String usage = new CommandLineParser().getUsage( commandInfo.getCommandName() );
    errorStream.write( usage.getBytes( ENCODING ) );
    errorStream.flush();
  }

  private String readErrorStream() {
    String result = null;
    byte[] bytes = errorStream.toByteArray();
    if( bytes.length != 0 ) {
      result = new String( bytes, ENCODING ).trim();
      if( result.startsWith( FATAL_PREFIX ) ) {
        result = result.substring( FATAL_PREFIX.length() );
      }
    }
    return result;
  }

  private void init( Repository repository ) {
    try {
      Method initMethod = COMMAND_TYPE.getDeclaredMethod( "init", Repository.class, String.class );
      initMethod.setAccessible( true );
      initMethod.invoke( commandInfo.getCommand(), repository, null );
    } catch( InvocationTargetException exception ) {
      Throwables.propagate( exception.getCause() );
    } catch( NoSuchMethodException | IllegalAccessException exception ) {
      throw new RuntimeException( exception );
    }
  }

  private void assignStream( String fieldName, OutputStream outputStream ) {
    for( Field field : COMMAND_TYPE.getDeclaredFields() ) {
      if( fieldName.equals( field.getName() ) ) {
        field.setAccessible( true );
        try {
          field.set( commandInfo.getCommand(), outputStream );
        } catch( IllegalAccessException e ) {
          throw new RuntimeException( e );
        }
      }
    }
  }
}