package com.codeaffine.gonsole.internal.interpreter;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Set;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class AliasConfig {

  private static final String ALIAS_SECTION = "alias";
  private final File repositoryLocation;

  public AliasConfig( File repositoryLocation ) {
    this.repositoryLocation = repositoryLocation;
  }

  public String[] getAliases() {
    Repository repository = openRepository();
    StoredConfig config = repository.getConfig();
    Set<String> aliases = config.getNames( ALIAS_SECTION, true );
    repository.close();
    return aliases.stream().toArray( String[]::new );
  }

  public String getCommand( String alias ) {
    Repository repository = openRepository();
    StoredConfig config = repository.getConfig();
    String result = config.getString( ALIAS_SECTION, null, alias );
    repository.close();
    return result;
  }

  private Repository openRepository() {
    Repository result;
    try {
      FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
      result = repositoryBuilder.setMustExist( true ).setGitDir( repositoryLocation ).build();
    } catch( IOException ignore ) {
      throw new UncheckedIOException( ignore );
    }
    return result;
  }
}
