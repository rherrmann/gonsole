package com.codeaffine.gonsole.internal.repository;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Sets.newLinkedHashSet;

import java.io.File;
import java.util.Arrays;
import java.util.Set;

import com.codeaffine.gonsole.RepositoryProvider;
import com.google.common.base.Objects;


public class CompositeRepositoryProvider implements RepositoryProvider {

  private final RepositoryProvider[] repositoryProviders;
  private final Set<RepositoryChangeListener> listeners;
  private File currentRepositoryLocation;

  public CompositeRepositoryProvider( RepositoryProvider... repositoryProviders ) {
    this.repositoryProviders = repositoryProviders;
    this.listeners = newHashSet();
  }

  @Override
  public File[] getRepositoryLocations() {
    Set<File> allLocations = newLinkedHashSet();
    for( RepositoryProvider repositoryProvider : repositoryProviders ) {
      addRepositoryLocations( allLocations, repositoryProvider.getRepositoryLocations() );
    }
    return toArray( allLocations, File.class );
  }

  public File getCurrentRepositoryLocation() {
    return currentRepositoryLocation;
  }

  public void setCurrentRepositoryLocation( File currentRepositoryLocation ) {
    if( !Objects.equal( this.currentRepositoryLocation, currentRepositoryLocation ) ) {
      this.currentRepositoryLocation = currentRepositoryLocation;
      sendCurrentRepositoryChangedEvent();
    }
  }

  public void addRepositoryChangeListener( RepositoryChangeListener listener ) {
    listeners.add( listener );
  }

  public void removeRepositoryChangeListener( RepositoryChangeListener listener ) {
    listeners.remove( listener );
  }

  private static void addRepositoryLocations( Set<File> allLocations, File[] locations ) {
    if( locations != null ) {
      allLocations.addAll( Arrays.asList( locations ) );
    }
  }

  private void sendCurrentRepositoryChangedEvent() {
    RepositoryChangeListener[] listenerArray = toArray( listeners, RepositoryChangeListener.class );
    for( RepositoryChangeListener listener : listenerArray ) {
      listener.currentRepositoryChanged();
    }
  }
}
