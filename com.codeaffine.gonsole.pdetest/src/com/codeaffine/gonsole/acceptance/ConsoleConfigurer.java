package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper.createWithSingleChildProvider;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.swt.widgets.Display;
import org.junit.rules.TemporaryFolder;

import com.codeaffine.console.core.ConsoleDefinition;
import com.codeaffine.gonsole.internal.GitConsoleDefinition;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

public class ConsoleConfigurer extends TemporaryFolder {

  public ConsoleDefinition create( String ... directories ) {
    File[] repositories = createRepositories( directories );
    CompositeRepositoryProvider repositoryProvider = createWithSingleChildProvider( repositories );
    return new GitConsoleDefinition( Display.getCurrent(), repositoryProvider );
  }

  public File[] createRepositories( String ... directories ) {
    File[] result = new File[ directories.length ];
    for( int i = 0; i < directories.length; i++ ) {
      result[ i ] = createRepository( directories[ i ] );
    }
    return result;
  }

  private File createRepository( String directory ) {
    Git git = initRepository( directory );
    File result = git.getRepository().getDirectory();
    git.getRepository().close();
    return result;
  }

  private Git initRepository( String directory ) {
    try {
      return Git.init().setDirectory( new File( getRoot(), directory ) ).call();
    } catch( GitAPIException gae ) {
      throw new RuntimeException( gae );
    }
  }
}