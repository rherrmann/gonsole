package com.codeaffine.gonsole.internal.repository;

import static com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper.createWithMultipleChildProviders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Test;

import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

public class CompositeRepositoryProviderTest {

  @Test
  public void testGetRepositoryLocations() {
    File location1 = new File( "a" );
    File location2 = new File( "b" );
    CompositeRepositoryProvider provider = createWithMultipleChildProviders( location1, location2 );

    File[] repositoryLocations = provider.getRepositoryLocations();

    assertThat( repositoryLocations )
      .hasSize( 2 )
      .contains( location1, location2 );
  }

  @Test
  public void testGetRepositoryLocationsWithDuplicates() {
    File location1 = new File( "x" );
    File location2 = new File( "x" );
    CompositeRepositoryProvider provider = createWithMultipleChildProviders( location1, location2 );

    File[] repositoryLocations = provider.getRepositoryLocations();

    assertThat( repositoryLocations ).containsExactly( location1 );
  }

  @Test
  public void testGetRepositoryLocationsWithNonConformingRepositoryProvider() {
    CompositeRepositoryProvider provider = createWithMultipleChildProviders();

    File[] repositoryLocations = provider.getRepositoryLocations();

    assertThat( repositoryLocations ).isEmpty();
  }

  @Test
  public void testInitialCurrentRepositoryLocation() {
    CompositeRepositoryProvider compositeProvider = new CompositeRepositoryProvider();

    File currentLocation = compositeProvider.getCurrentRepositoryLocation();

    assertThat( currentLocation ).isNull();
  }

  @Test
  public void testCurrentRepositoryLocation() {
    File location = new File( "a" );
    CompositeRepositoryProvider compositeProvider = new CompositeRepositoryProvider();

    compositeProvider.setCurrentRepositoryLocation( location );

    assertThat( compositeProvider.getCurrentRepositoryLocation() ).isSameAs( location );
  }

  @Test
  public void testSetCurrentRepositoryLocationToCurrentValue() {
    File location = new File( "a" );
    CompositeRepositoryProvider compositeProvider = new CompositeRepositoryProvider();
    compositeProvider.setCurrentRepositoryLocation( location );
    RepositoryChangeListener listener = mock( RepositoryChangeListener.class );
    compositeProvider.addRepositoryChangeListener( listener );

    compositeProvider.setCurrentRepositoryLocation( location );

    verify( listener, never() ).currentRepositoryChanged();
  }

  @Test
  public void testAddRemoveRepositoryChangeListener() {
    CompositeRepositoryProvider compositeProvider = new CompositeRepositoryProvider();
    RepositoryChangeListener listener = mock( RepositoryChangeListener.class );
    compositeProvider.addRepositoryChangeListener( listener );

    compositeProvider.setCurrentRepositoryLocation( new File( "a" ) );

    verify( listener ).currentRepositoryChanged();
  }

  @Test
  public void testRemoveRepositoryChangeListener() {
    CompositeRepositoryProvider compositeProvider = new CompositeRepositoryProvider();
    RepositoryChangeListener listener = mock( RepositoryChangeListener.class );
    compositeProvider.addRepositoryChangeListener( listener );
    compositeProvider.removeRepositoryChangeListener( listener );

    compositeProvider.setCurrentRepositoryLocation( new File( "a" ) );

    verify( listener, never() ).currentRepositoryChanged();
  }
}