package com.codeaffine.gonsole.internal.gitconsole.repository;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import com.codeaffine.gonsole.RepositoryProvider;

public class RepositoryProviderExtensionReader {

  private static final String EP_REPOSITORY_PROVIDERS
    = "com.codeaffine.gonsole.repositoryProviders";

  public RepositoryProvider[] read() {
    IConfigurationElement[] elements = getRepositoryProviderConfigElements();
    return createRepositoryProviders( elements );
  }

  private static RepositoryProvider[] createRepositoryProviders( IConfigurationElement[] elements ) {
    RepositoryProvider[] result = new RepositoryProvider[ elements.length ];
    for( int i = 0; i < result.length; i++ ) {
      result[ i ] = createRepositoryProvider( elements[ i ] );
    }
    return result;
  }

  private static RepositoryProvider createRepositoryProvider( IConfigurationElement element ) {
    try {
      return ( RepositoryProvider )element.createExecutableExtension( "class" );
    } catch( CoreException e ) {
      throw new RuntimeException( e );
    }
  }

  private static IConfigurationElement[] getRepositoryProviderConfigElements() {
    return Platform.getExtensionRegistry().getConfigurationElementsFor( EP_REPOSITORY_PROVIDERS );
  }
}
