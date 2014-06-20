package com.codeaffine.gonsole.test.helper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import com.codeaffine.gonsole.RepositoryProvider;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

public class CompositeRepositoryProviderHelper {

  public static CompositeRepositoryProvider createWithSingleChildProvider( File ... repositoryLocations ) {
    RepositoryProvider repositoryProvider = stubRepositoryProvider( repositoryLocations );
    RepositoryProvider[] providers = new RepositoryProvider[] { repositoryProvider };
    return createCompositeProvider( providers, getCurrentLocation( repositoryLocations ) );
  }

  public static CompositeRepositoryProvider createWithMultipleChildProviders( File ... repositoryLocations ) {
    RepositoryProvider[] providers = new RepositoryProvider[ repositoryLocations.length ];
    for( int i = 0; i < repositoryLocations.length; i++ ) {
      providers[ i ] = stubRepositoryProvider( repositoryLocations );
    }
    return createCompositeProvider( providers, getCurrentLocation( repositoryLocations ) );
  }

  public static RepositoryProvider stubRepositoryProvider( File ... repositoryLocations ) {
    RepositoryProvider result = mock( RepositoryProvider.class );
    when( result.getRepositoryLocations() ).thenReturn( repositoryLocations );
    return result;
  }

  private static CompositeRepositoryProvider createCompositeProvider( RepositoryProvider[] providers, File current ) {
    CompositeRepositoryProvider result = new CompositeRepositoryProvider( providers );
    result.setCurrentRepositoryLocation( current );
    return result;
  }

  private static File getCurrentLocation( File ... repositoryLocations ) {
    return repositoryLocations.length > 0 ? repositoryLocations[ 0 ] : null;
  }
}