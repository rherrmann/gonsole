package com.codeaffine.gonsole.internal.gitconsole.repository;

import static com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper.createWithMultipleChildProviders;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

import com.codeaffine.gonsole.internal.gitconsole.repository.CompositeRepositoryProvider;

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
}