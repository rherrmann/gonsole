package com.codeaffine.gonsole.internal.gitconsole.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.assertj.core.api.Condition;
import org.junit.Test;

import com.codeaffine.gonsole.RepositoryProvider;
import com.codeaffine.gonsole.internal.gitconsole.repository.RepositoryProviderExtensionReader;

public class RepositoryProviderExtensionReaderPDETest {

  @Test
  public void testRead() {
    RepositoryProvider[] repositoryProviders = new RepositoryProviderExtensionReader().read();

    assertThat( repositoryProviders ).areAtLeast( 1, new Condition<RepositoryProvider>() {
      @Override
      public boolean matches( RepositoryProvider value ) {
        return value instanceof TestRepositoryProvider;
      }
    } );
  }

  public static class TestRepositoryProvider implements RepositoryProvider {
    @Override
    public File[] getRepositoryLocations() {
      return null;
    }
  }
}
