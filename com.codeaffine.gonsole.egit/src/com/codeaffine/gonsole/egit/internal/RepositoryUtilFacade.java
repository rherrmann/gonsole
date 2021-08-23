package com.codeaffine.gonsole.egit.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.RepositoryUtil;

@SuppressWarnings("restriction")
public class RepositoryUtilFacade {

  public static RepositoryUtil getRepositoryUtil() {
    return repositoryUtilV4()
        .orElse( repositoryUtilV5().orElseThrow( () -> new IllegalStateException( failureMessage() ) ) );
  }

  private static Optional<RepositoryUtil> repositoryUtilV4() {
    Activator activator = Activator.getDefault();
    try {
      Method method = activator.getClass().getMethod( "getRepositoryUtil" );
      return Optional.of(  ( RepositoryUtil )method.invoke( activator ) );
    } catch( NoSuchMethodException nsme ) {
      return Optional.empty();
    } catch( SecurityException | IllegalAccessException | InvocationTargetException e ) {
      throw new RuntimeException( failureMessage(), e );
    }
  }

  private static Optional<RepositoryUtil> repositoryUtilV5() {
    try {
      Method method = RepositoryUtil.class.getMethod( "getInstance" );
      return Optional.of( ( RepositoryUtil )method.invoke( null ) );
    } catch( NoSuchMethodException nsme ) {
      return Optional.empty();
    } catch( SecurityException | IllegalAccessException | InvocationTargetException e ) {
      throw new RuntimeException( failureMessage(), e );
    }
  }

  private static String failureMessage() {
    return "Failed to list configured EGit repositories";
  }

}
