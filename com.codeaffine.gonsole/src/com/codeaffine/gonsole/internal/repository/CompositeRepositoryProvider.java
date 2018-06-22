package com.codeaffine.gonsole.internal.repository;

import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.codeaffine.gonsole.RepositoryProvider;


public class CompositeRepositoryProvider implements RepositoryProvider {

  private final RepositoryProvider[] repositoryProviders;
  private final Set<RepositoryChangeListener> listeners;
  private File currentRepositoryLocation;

  public CompositeRepositoryProvider( RepositoryProvider... repositoryProviders ) {
    this.repositoryProviders = repositoryProviders;
    this.listeners = new HashSet<>();
  }

  @Override
  public File[] getRepositoryLocations() {
    Set<File> set = Stream.of( repositoryProviders )
      .flatMap( repositoryProvider -> stream( repositoryProvider ) )
      .collect( toSet() );
    return set.stream().sorted().toArray( File[]::new );
  }

  public File getCurrentRepositoryLocation() {
    return currentRepositoryLocation;
  }

  public void setCurrentRepositoryLocation( File currentRepositoryLocation ) {
    if( !Objects.equals( this.currentRepositoryLocation, currentRepositoryLocation ) ) {
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

  private void sendCurrentRepositoryChangedEvent() {
    RepositoryChangeListener[] listenerArray = listeners.stream().toArray( RepositoryChangeListener[]::new );
    for( RepositoryChangeListener listener : listenerArray ) {
      listener.currentRepositoryChanged();
    }
  }

  private static Stream<File> stream( RepositoryProvider repositoryProvider ) {
    File[] repositoryLocations = repositoryProvider.getRepositoryLocations();
    return repositoryLocations == null ? Stream.empty() : Stream.of( repositoryLocations );
  }
}
