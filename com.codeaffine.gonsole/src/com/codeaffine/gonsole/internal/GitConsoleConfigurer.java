package com.codeaffine.gonsole.internal;

import static com.codeaffine.gonsole.internal.activator.IconRegistry.GONSOLE;

import java.io.File;

import org.eclipse.swt.widgets.Display;

import com.codeaffine.console.core.Console;
import com.codeaffine.console.core.ConsoleConfigurer;
import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.internal.repository.RepositoryChangeListener;
import com.codeaffine.gonsole.util.Repositories;

public class GitConsoleConfigurer implements ConsoleConfigurer {

  private final CompositeRepositoryProvider repositoryProvider;
  private final Display display;
  private Console console;

  public GitConsoleConfigurer( Display display, CompositeRepositoryProvider repositoryProvider ) {
    this.repositoryProvider = repositoryProvider;
    this.display = display;
  }

  public CompositeRepositoryProvider getRepositoryProvider() {
    return repositoryProvider;
  }

  @Override
  public void configure( Console console ) {
    this.console = console;
    updateTitle();
    console.setImageDescriptor( new IconRegistry().getDescriptor( GONSOLE ) );
    console.setColorScheme( new GitConsoleColorScheme() );
    console.setConsoleComponentFactory( new GitConsoleComponentFactory( repositoryProvider ) );
    repositoryProvider.addRepositoryChangeListener( new CurrentRepositoryChangeObserver() );
  }

  private void execUpdateTitle() {
    display.asyncExec( this::updateTitle );
  }

  private void updateTitle() {
    if( !console.isDisposed() ) {
      console.setTitle( getTitle() );
    }
  }

  private String getTitle() {
    File currentRepositoryLocation = repositoryProvider.getCurrentRepositoryLocation();
    String repositoryName = Repositories.getRepositoryName( currentRepositoryLocation );
    return repositoryName == null
        ? "No repository selected, type 'use <repository>' to change the current repository"
        : "Git Console: " + repositoryName;
  }

  private class CurrentRepositoryChangeObserver implements RepositoryChangeListener {
    @Override
    public void currentRepositoryChanged() {
      execUpdateTitle();
    }
  }
}