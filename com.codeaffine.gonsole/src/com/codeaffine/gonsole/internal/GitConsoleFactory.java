package com.codeaffine.gonsole.internal;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;

import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.internal.repository.TempRepositoryProvider;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


public class GitConsoleFactory implements IConsoleFactory {

  private final IConsoleManager consoleManager;


  public GitConsoleFactory() {
    consoleManager = ConsolePlugin.getDefault().getConsoleManager();
  }

  @Override
  public void openConsole() {
    GitConsole console = getConsole();
    consoleManager.showConsoleView( console );
  }

  private GitConsole getConsole() {
    GitConsole result = getExistingConsole();
    if( result == null ) {
      result = createConsole();
    }
    return result;
  }

  private GitConsole getExistingConsole() {
    Predicate<IConsole> predicate = new Predicate<IConsole>() {
      @Override
      public boolean apply( IConsole input ) {
        return input instanceof GitConsole;
      }
    };
    return ( GitConsole )Iterables.find( getAllConsoles(), predicate, null );
  }

  private Collection<IConsole> getAllConsoles() {
    return Arrays.asList( consoleManager.getConsoles() );
  }

  private static GitConsole createConsole() {
    TempRepositoryProvider tempRepositoryProvider = new TempRepositoryProvider();
    tempRepositoryProvider.ensureRepositories();
    CompositeRepositoryProvider repositoryProvider = new CompositeRepositoryProvider( tempRepositoryProvider );
    repositoryProvider.setCurrentRepositoryLocation( tempRepositoryProvider.getRepositoryLocations()[ 0 ] );

    GitConsole result = new GitConsole( repositoryProvider );
    ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[] { result } );
    return result;
  }
}
