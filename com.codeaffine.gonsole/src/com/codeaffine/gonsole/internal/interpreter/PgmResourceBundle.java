package com.codeaffine.gonsole.internal.interpreter;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jgit.errors.TranslationBundleLoadingException;
import org.eclipse.jgit.nls.NLS;
import org.eclipse.jgit.nls.TranslationBundle;
import org.eclipse.jgit.pgm.internal.CLIText;

import com.google.common.base.Throwables;

/*
 * Workaround for JGit bug 436232: NLS cannot be used in OSGi
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=436232
 */
@SuppressWarnings("restriction")
public class PgmResourceBundle {

  public void initialize() {
    try {
      intialize();
    } catch( Exception e ) {
      Throwables.propagate( e );
    }
  }

  public void reset() {
    try {
      getMap( getNlsForCurrentThread() ).remove( CLIText.class );
    } catch( Exception e ) {
      Throwables.propagate( e );
    }
  }

  public ResourceBundle getResourceBundle() {
    return CLIText.get().resourceBundle();
  }

  private static void intialize() throws NoSuchFieldException, IllegalAccessException {
    NLS.useJVMDefaultLocale();
    NLS nls = getNlsForCurrentThread();
    CLIText cliText = loadPgmTranslation();
    storeTranslationBundle( nls, cliText );
  }

  private static NLS getNlsForCurrentThread() throws NoSuchFieldException, IllegalAccessException {
    Field localField = NLS.class.getDeclaredField( "local" );
    localField.setAccessible( true );
    ThreadLocal<?> nlsThreadLocal = ( ThreadLocal<?> )localField.get( null );
    return ( NLS )nlsThreadLocal.get();
  }

  private static CLIText loadPgmTranslation()
    throws TranslationBundleLoadingException, IllegalAccessException
  {
    CLIText result = new CLIText();
    load( result );
    return result;
  }

  private static void storeTranslationBundle( NLS nls, CLIText cliText )
    throws NoSuchFieldException, IllegalAccessException
  {
    Map<Object, Object> map = getMap( nls );
    map.put( CLIText.class, cliText );
  }

  @SuppressWarnings("unchecked")
  private static Map<Object, Object> getMap( NLS nls )
    throws NoSuchFieldException, IllegalAccessException
  {
    Field mapField = nls.getClass().getDeclaredField( "map" );
    mapField.setAccessible( true );
    return ( Map<Object,Object> )mapField.get( nls );
  }

  private static void load( TranslationBundle translationBundle )
    throws TranslationBundleLoadingException, IllegalAccessException
  {
    Class<?> bundleClass = translationBundle.getClass();
    ResourceBundle resourceBundle = getResourceBundle( bundleClass );
    for( Field field : bundleClass.getFields() ) {
      if( field.getType().equals( String.class ) ) {
        field.set( translationBundle, resourceBundle.getString( field.getName() ) );
      }
    }

    try {
      Field field = TranslationBundle.class.getDeclaredField( "resourceBundle" );
      field.setAccessible( true );
      field.set( translationBundle, resourceBundle );
    } catch( Exception e ) {
    }
  }

  private static ResourceBundle getResourceBundle( Class<?> bundleClass ) {
    try {
      return ResourceBundle.getBundle( bundleClass.getName(),
                                       Locale.getDefault(),
                                       bundleClass.getClassLoader() );
    } catch( MissingResourceException e ) {
      throw new RuntimeException( e );
    }
  }
}