package com.codeaffine.gonsole.internal.repository;

import java.util.Collection;

import com.codeaffine.eclipse.core.runtime.RegistryAdapter;
import com.codeaffine.gonsole.RepositoryProvider;

public class RepositoryProviderExtensionReader {

  public RepositoryProvider[] read() {
    return doRead().stream().toArray( RepositoryProvider[]::new );
  }

  private static Collection<RepositoryProvider> doRead() {
    return new RegistryAdapter()
      .createExecutableExtensions( "com.codeaffine.gonsole.repositoryProviders",RepositoryProvider.class )
      .process();
  }
}