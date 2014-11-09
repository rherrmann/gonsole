package com.codeaffine.gonsole.internal.repository;

import static com.google.common.collect.Iterables.toArray;

import java.util.Collection;

import com.codeaffine.eclipse.core.runtime.RegistryAdapter;
import com.codeaffine.gonsole.RepositoryProvider;

public class RepositoryProviderExtensionReader {

  public RepositoryProvider[] read() {
    return toArray( doRead(), RepositoryProvider.class );
  }

  private static Collection<RepositoryProvider> doRead() {
    return new RegistryAdapter()
      .createExecutableExtensions( "com.codeaffine.gonsole.repositoryProviders",RepositoryProvider.class )
      .process();
  }
}