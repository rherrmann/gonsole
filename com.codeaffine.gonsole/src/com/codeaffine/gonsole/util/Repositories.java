package com.codeaffine.gonsole.util;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;


public class Repositories {

  public static IPath getWorkDir( File repositoryLocation ) {
    IPath result = null;
    FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
    repositoryBuilder.setMustExist( true );
    repositoryBuilder.setGitDir( repositoryLocation );
    try {
      Repository repository = repositoryBuilder.build();
      if( !repository.isBare() ) {
        result = new Path( repository.getWorkTree().getCanonicalPath() );
      }
      repository.close();
    } catch( IOException ignore ) {
    }
    return result;
  }
}
