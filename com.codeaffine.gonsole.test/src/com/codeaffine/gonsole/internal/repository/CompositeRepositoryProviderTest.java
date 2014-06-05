package com.codeaffine.gonsole.internal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;

import com.codeaffine.gonsole.RepositoryProvider;

public class CompositeRepositoryProviderTest {

  @Test
  public void testGetRepositoryLocations() {
    File location1 = new File( "a" );
    RepositoryProvider provider1 = createRepositoryProvider( location1 );
    File location2 = new File( "b" );
    RepositoryProvider provider2 = createRepositoryProvider( location2 );

    File[] repositoryLocations = getRepositoryLocations( provider1, provider2 );

    assertThat( repositoryLocations ).hasSize( 2 );
    assertThat( repositoryLocations ).contains( location1 );
    assertThat( repositoryLocations ).contains( location2 );
  }

  @Test
  public void testGetRepositoryLocationsWithDuplicates() {
    File location1 = new File( "x" );
    RepositoryProvider provider1 = createRepositoryProvider( location1 );
    File location2 = new File( "x" );
    RepositoryProvider provider2 = createRepositoryProvider( location2 );

    File[] repositoryLocations = getRepositoryLocations( provider1, provider2 );

    assertThat( repositoryLocations ).containsExactly( location1 );
  }

  @Test
  public void testGetRepositoryLocationsWithNonConformingRepositoryProvider() {
    RepositoryProvider provider = mock( RepositoryProvider.class );

    File[] repositoryLocations = getRepositoryLocations( provider );

    assertThat( repositoryLocations ).isEmpty();
  }

  @Test
  public void testInitialCurrentRepositoryLocation() {
    CompositeRepositoryProvider compositeProvider = new CompositeRepositoryProvider();

    assertThat( compositeProvider.getCurrentRepositoryLocation() ).isNull();
  }

  @Test
  public void testCurrentRepositoryLocation() {
    File location = new File( "a" );
    CompositeRepositoryProvider compositeProvider = new CompositeRepositoryProvider();

    compositeProvider.setCurrentRepositoryLocation( location );

    assertThat( compositeProvider.getCurrentRepositoryLocation() ).isSameAs( location );
  }

  private static RepositoryProvider createRepositoryProvider( File... locations ) {
    RepositoryProvider result = mock( RepositoryProvider.class );
    when( result.getRepositoryLocations() ).thenReturn( locations );
    return result;
  }

  private static File[] getRepositoryLocations( RepositoryProvider... repositoryProviders ) {
    RepositoryProvider compositeProvider = new CompositeRepositoryProvider( repositoryProviders );
    return compositeProvider.getRepositoryLocations();
  }
}
