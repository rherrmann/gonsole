package com.codeaffine.gonsole.internal.repository;

import java.io.File;

import com.codeaffine.gonsole.RepositoryProvider;

public class CompositeRepositoryProviderFactory {

  private final RepositoryProviderExtensionReader extensionReader;

  public CompositeRepositoryProviderFactory() {
    this( new RepositoryProviderExtensionReader() );
  }

  CompositeRepositoryProviderFactory( RepositoryProviderExtensionReader extensionReader ) {
    this.extensionReader = extensionReader;
  }

  public CompositeRepositoryProvider create() {
    RepositoryProvider[] repositoryProviders = extensionReader.read();
    CompositeRepositoryProvider result = new CompositeRepositoryProvider( repositoryProviders );
    updateCurrentRepositoryLocation( result );
    return result;
  }

  private static void updateCurrentRepositoryLocation( CompositeRepositoryProvider repositoryProvider ) {
    File[] repositoryLocations = repositoryProvider.getRepositoryLocations();
    if( repositoryLocations.length > 0 ) {
      repositoryProvider.setCurrentRepositoryLocation( repositoryLocations[ 0 ] );
    }
  }
}