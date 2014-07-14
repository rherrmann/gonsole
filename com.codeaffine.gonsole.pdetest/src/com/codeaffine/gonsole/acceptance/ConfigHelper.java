package com.codeaffine.gonsole.acceptance;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

class ConfigHelper {
  private final File repositoryLocation;

  ConfigHelper( File repositoryLocation ) {
    this.repositoryLocation = repositoryLocation;
  }

  void setValue( String section, String key, String value ) throws IOException {
    Repository repository = openRepository();
    try {
      StoredConfig config = repository.getConfig();
      config.setString( section, null, key, value );
      config.save();
    } finally {
      repository.close();
    }
  }

  private Repository openRepository() throws IOException {
    FileRepositoryBuilder fileRepositoryBuilder = new FileRepositoryBuilder();
    return fileRepositoryBuilder.setMustExist( true ).setGitDir( repositoryLocation ).build();
  }
}