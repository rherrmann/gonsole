package com.codeaffine.gonsole.internal.gitconsole.repository;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Sets.newHashSet;

import java.io.File;
import java.util.Arrays;
import java.util.Set;

import com.codeaffine.gonsole.RepositoryProvider;


public class CompositeRepositoryProvider implements RepositoryProvider {

  private final RepositoryProvider[] repositoryProviders;
  private File currentRepositoryLocation;

  public CompositeRepositoryProvider( RepositoryProvider... repositoryProviders ) {
    this.repositoryProviders = repositoryProviders;
  }

  @Override
  public File[] getRepositoryLocations() {
    Set<File> allLocations = newHashSet();
    for( RepositoryProvider repositoryProvider : repositoryProviders ) {
      addRepositoryLocations( allLocations, repositoryProvider.getRepositoryLocations() );
    }
    return toArray( allLocations, File.class );
  }

  public File getCurrentRepositoryLocation() {
    return currentRepositoryLocation;
  }

  public void setCurrentRepositoryLocation( File currentRepositoryLocation ) {
    this.currentRepositoryLocation = currentRepositoryLocation;
  }

  private static void addRepositoryLocations( Set<File> allLocations, File[] locations ) {
    if( locations != null ) {
      allLocations.addAll( Arrays.asList( locations ) );
    }
  }
}
