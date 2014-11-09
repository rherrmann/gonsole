package com.codeaffine.gonsole.egit.internal.repositores;

import static com.codeaffine.gonsole.test.io.Files.toCanonicalFile;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.egit.pdetest.EGitRepositoryHelper;


public class EGitRepositoryProviderPDETest {

  @Rule
  public final EGitRepositoryHelper repositoryHelper = new EGitRepositoryHelper();

  @Test
  public void testGetRepositoryLocations() {
    File repositoryLocation = repositoryHelper.createRegisteredRepository( "repo" );

    File[] repositoryLocations = new EGitRepositoryProvider().getRepositoryLocations();

    assertThat( repositoryLocations ).contains( toCanonicalFile( repositoryLocation ) );
  }
}