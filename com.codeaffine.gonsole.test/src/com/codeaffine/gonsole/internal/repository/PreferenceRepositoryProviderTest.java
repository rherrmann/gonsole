package com.codeaffine.gonsole.internal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.RepositoryProvider;
import com.codeaffine.gonsole.internal.preference.WorkspaceScopePreferences;

public class PreferenceRepositoryProviderTest {

  private WorkspaceScopePreferences preferences;
  private RepositoryProvider repositoryProvider;

  @Test
  public void testGetRepositoryLocationsWithEmptyPreferences() {
    when( preferences.getRepositoryLocations() ).thenReturn( "" );

    File[] repositoryLocations = repositoryProvider.getRepositoryLocations();

    assertThat( repositoryLocations ).isEmpty();
  }

  @Test
  public void testGetRepositoryLocationsWithFilledPreferences() {
    when( preferences.getRepositoryLocations() ).thenReturn( "a\nb" );

    File[] repositoryLocations = repositoryProvider.getRepositoryLocations();

    assertThat( repositoryLocations ).contains( new File( "a" ), new File( "b" ) );
  }

  @Test
  public void testGetRepositoryLocationsWithEmptyLinesInPreferences() {
    when( preferences.getRepositoryLocations() ).thenReturn( "a\n\nb\n\n" );

    File[] repositoryLocations = repositoryProvider.getRepositoryLocations();

    assertThat( repositoryLocations ).contains( new File( "a" ), new File( "b" ) );
  }

  @Test
  public void testGetRepositoryLocationsWithMixedNewLinesInPreferences() {
    when( preferences.getRepositoryLocations() ).thenReturn( "a\r\nb\n" );

    File[] repositoryLocations = repositoryProvider.getRepositoryLocations();

    assertThat( repositoryLocations ).contains( new File( "a" ), new File( "b" ) );
  }

  @Test
  public void testGetRepositoryLocationsWithTrailingSpacesInPreferences() {
    when( preferences.getRepositoryLocations() ).thenReturn( "a   " );

    File[] repositoryLocations = repositoryProvider.getRepositoryLocations();

    assertThat( repositoryLocations ).contains( new File( "a" ) );
  }

  @Before
  public void setUp() {
    preferences = mock( WorkspaceScopePreferences.class );
    repositoryProvider = new PreferenceRepositoryProvider( preferences );
  }
}
