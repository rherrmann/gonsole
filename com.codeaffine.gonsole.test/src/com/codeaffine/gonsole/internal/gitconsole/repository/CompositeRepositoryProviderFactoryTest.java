package com.codeaffine.gonsole.internal.gitconsole.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.RepositoryProvider;
import com.codeaffine.gonsole.internal.gitconsole.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.internal.gitconsole.repository.CompositeRepositoryProviderFactory;
import com.codeaffine.gonsole.internal.gitconsole.repository.RepositoryProviderExtensionReader;
import com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper;

public class CompositeRepositoryProviderFactoryTest {

  private RepositoryProviderExtensionReader extensionReader;
  private CompositeRepositoryProviderFactory factory;

  @Test
  public void testCreateWithRepositoryProviderContribution() {
    File expected = new File( "a" );
    equipExtensionReaderWithLocation( expected );

    CompositeRepositoryProvider actual = factory.create();

    assertThat( actual.getRepositoryLocations() ).containsExactly( expected );
    assertThat( actual.getCurrentRepositoryLocation() ).isEqualTo( expected );
  }

  @Test
  public  void testCreateWithoutRepositoryProviderContribution() {
    equipExtensionReaderWithLocation();

    CompositeRepositoryProvider actual = factory.create();

    assertThat( actual.getRepositoryLocations() ).isEmpty();
    assertThat( actual.getCurrentRepositoryLocation() ).isNull();
  }

  @Before
  public void setUp() {
    extensionReader = mock( RepositoryProviderExtensionReader.class );
    factory = new CompositeRepositoryProviderFactory( extensionReader );
  }

  private void equipExtensionReaderWithLocation( File... locations ) {
    RepositoryProvider provider = CompositeRepositoryProviderHelper.stubRepositoryProvider( locations );
    when( extensionReader.read() ).thenReturn( new RepositoryProvider[] { provider } );
  }
}