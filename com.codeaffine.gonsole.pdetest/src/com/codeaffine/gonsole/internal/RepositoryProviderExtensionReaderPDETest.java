package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Condition;
import org.junit.Test;

import com.codeaffine.gonsole.RepositoryProvider;
import com.codeaffine.gonsole.internal.repository.RepositoryProviderExtensionReader;

public class RepositoryProviderExtensionReaderPDETest {

  @Test
  public void testRead() {
    RepositoryProvider[] repositoryProviders = new RepositoryProviderExtensionReader().read();

    assertThat( repositoryProviders ).have( new Condition<RepositoryProvider>() {
      @Override
      public boolean matches( RepositoryProvider value ) {
        return value instanceof TestRepositoryProvider;
      }
    } );
  }

  public static class TestRepositoryProvider implements RepositoryProvider {
  }
}
