package com.codeaffine.gonsole.internal;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;

import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.internal.repository.TempRepositoryProvider;


public class GitConsoleFactory implements IConsoleFactory {

  @Override
  public void openConsole() {
    TempRepositoryProvider tempRepositoryProvider = new TempRepositoryProvider();
    tempRepositoryProvider.ensureRepositories();
    CompositeRepositoryProvider repositoryProvider = new CompositeRepositoryProvider( tempRepositoryProvider );
    repositoryProvider.setCurrentRepositoryLocation( tempRepositoryProvider.getRepositoryLocations()[ 0 ] );

    IConsole console = new GitConsole( repositoryProvider );
    ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[] { console } );
  }

}
