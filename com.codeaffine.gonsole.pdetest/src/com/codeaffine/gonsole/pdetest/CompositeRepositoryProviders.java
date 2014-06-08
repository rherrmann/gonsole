package com.codeaffine.gonsole.pdetest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import com.codeaffine.gonsole.RepositoryProvider;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

class CompositeRepositoryProviders {

  static CompositeRepositoryProvider createFor( File[] repositoryLocations ) {
    RepositoryProvider repositoryProvider = stubRepositoryProvider( repositoryLocations );
    CompositeRepositoryProvider result = new CompositeRepositoryProvider( repositoryProvider );
    result.setCurrentRepositoryLocation( repositoryLocations[ 0 ] );
    return result;
  }

  private static RepositoryProvider stubRepositoryProvider( File[] repositoryLocations ) {
    RepositoryProvider result = mock( RepositoryProvider.class );
    when( result.getRepositoryLocations() ).thenReturn( repositoryLocations );
    return result;
  }
}