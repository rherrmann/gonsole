package com.codeaffine.gonsole.egit.internal.repositores;

import java.io.File;


import com.codeaffine.gonsole.RepositoryProvider;
import com.codeaffine.gonsole.egit.internal.EGitFacade;


public class EGitRepositoryProvider implements RepositoryProvider {

  @Override
  public File[] getRepositoryLocations() {
    return new EGitFacade().getConfiguredRepositoryLocations();
  }

}
