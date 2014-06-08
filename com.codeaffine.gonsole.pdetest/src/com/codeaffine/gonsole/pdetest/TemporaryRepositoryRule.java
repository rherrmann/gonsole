package com.codeaffine.gonsole.pdetest;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.rules.TemporaryFolder;

public class TemporaryRepositoryRule extends TemporaryFolder {

  public File[] create( String ... directories ) throws GitAPIException {
    File[] result = new File[ directories.length ];
    for( int i = 0; i < directories.length; i++ ) {
      result[ i ] = createRepository( directories[ i ] );
    }
    return result;
  }

  private File createRepository( String directory ) throws GitAPIException {
    Git git = Git.init().setDirectory( new File( getRoot(), directory ) ).call();
    File result = git.getRepository().getDirectory();
    git.getRepository().close();
    return result;
  }
}