package com.codeaffine.gonsole.internal;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jgit.errors.TranslationBundleLoadingException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.nls.NLS;
import org.eclipse.jgit.nls.TranslationBundle;
import org.eclipse.jgit.pgm.TextBuiltin;
import org.eclipse.jgit.pgm.internal.CLIText;
import org.eclipse.jgit.pgm.opt.CmdLineParser;
import org.eclipse.jgit.pgm.opt.SubcommandHandler;
import org.kohsuke.args4j.Argument;

import com.google.common.base.Throwables;

public class GitInterpreter {

  @Argument(index = 0, metaVar = "metaVar_command", required = true, handler = SubcommandHandler.class)
  public TextBuiltin subcommand;
  @Argument(index = 1, metaVar = "metaVar_arg")
  private final List<String> arguments = new ArrayList<String>();

  private final OutputStream outputStream;
  private final String directory;

  public GitInterpreter( OutputStream outputStream, String directory ) {
    this.outputStream = outputStream;
    this.directory = directory;
  }

  public void execute( String... commands ) {
    try {
      NLS.useJVMDefaultLocale();
      Field localField = NLS.class.getDeclaredField( "local" );
      localField.setAccessible( true );
      ThreadLocal<?> nlsThreadLocal = ( ThreadLocal<?> )localField.get( null );
      NLS nls = ( NLS )nlsThreadLocal.get();
      Field mapField = nls.getClass().getDeclaredField( "map" );
      mapField.setAccessible( true );
      Map<Object,Object> map = ( Map<Object,Object> )mapField.get( nls );
      CLIText cliText = new CLIText();
      load( cliText );
      map.put( CLIText.class, cliText );
      CmdLineParser clp = new CmdLineParser( this );
      clp.parseArgument( commands );
      RepositoryBuilder rb = new RepositoryBuilder()
        .setWorkTree( new File( directory ) );
      Repository repository = rb.build();
      Field[] fields = TextBuiltin.class.getDeclaredFields();
      for( Field field : fields ) {
        if( "outs".equals( field.getName() ) || "errs".equals( field.getName() ) ) {
          field.setAccessible( true );
          field.set( subcommand, outputStream );
        }
      }
      initCommand( repository );
      subcommand.execute( arguments.toArray( new String[ arguments.size() ] ) );
    } catch( Exception exception ) {
      exception.printStackTrace();
    } finally {
      flushWriters();
      try {
        outputStream.flush();
      } catch( IOException ignore ) {
        ignore.printStackTrace();
      }
    }
  }

  private void initCommand( Repository repository ) {
    try {
      Method initMethod = TextBuiltin.class.getDeclaredMethod( "init", Repository.class, String.class );
      initMethod.setAccessible( true );
      initMethod.invoke( subcommand, repository, null );
    } catch( Exception exception ) {
      Throwables.propagate( exception );
    }
  }

  private void flushWriters() {
    try {
      Field[] fields = TextBuiltin.class.getDeclaredFields();
      for( Field field : fields ) {
        if( "outw".equals( field.getName() ) || "errw".equals( field.getName() ) ) {
          field.setAccessible( true );
          Writer writer = ( Writer )field.get( subcommand );
          writer.flush();
        }
      }
    } catch( Exception exception ) {
      Throwables.propagate( exception );
    }
  }

  private static void load( TranslationBundle translationBundle )
    throws TranslationBundleLoadingException
  {
    Class<?> bundleClass = translationBundle.getClass();
    ResourceBundle resourceBundle;
    try {
      resourceBundle = ResourceBundle.getBundle( bundleClass.getName(),
                                                 Locale.getDefault(),
                                                 bundleClass.getClassLoader() );
    } catch( MissingResourceException e ) {
      throw new RuntimeException( e );
    }
    for( Field field : bundleClass.getFields() ) {
      if( field.getType().equals( String.class ) ) {
        try {
          String translatedText = resourceBundle.getString( field.getName() );
          field.set( translationBundle, translatedText );
        } catch( MissingResourceException e ) {
          throw new RuntimeException( e );
        } catch( IllegalArgumentException e ) {
          throw new Error( e );
        } catch( IllegalAccessException e ) {
          throw new Error( e );
        }
      }
    }
  }
}