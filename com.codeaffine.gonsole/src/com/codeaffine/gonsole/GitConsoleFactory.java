package com.codeaffine.gonsole;

import java.io.File;

import org.eclipse.swt.widgets.Display;

import com.codeaffine.console.core.ConsoleConfigurer;
import com.codeaffine.console.core.ConsoleFactory;
import com.codeaffine.gonsole.internal.GitConsoleConfigurer;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProviderFactory;

public class GitConsoleFactory extends ConsoleFactory {

  private final File initialRepositoryLocation;

  public GitConsoleFactory() {
    this( null );
  }

  public GitConsoleFactory( File initialRepositoryLocation ) {
    this.initialRepositoryLocation = initialRepositoryLocation;
  }

  @Override
  protected ConsoleConfigurer getConsoleConfigurer() {
    CompositeRepositoryProvider repositoryProvider = createCompositeRepositoryProvider();
    return new GitConsoleConfigurer( Display.getCurrent(), repositoryProvider );
  }

  private CompositeRepositoryProvider createCompositeRepositoryProvider() {
    CompositeRepositoryProvider result = new CompositeRepositoryProviderFactory().create();
    result.setCurrentRepositoryLocation( initialRepositoryLocation );
    return result;
  }
}