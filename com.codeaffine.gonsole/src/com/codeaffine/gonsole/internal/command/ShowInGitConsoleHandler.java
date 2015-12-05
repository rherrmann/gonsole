package com.codeaffine.gonsole.internal.command;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;

import com.codeaffine.console.core.Console;
import com.codeaffine.console.core.ConsoleViewOpener;
import com.codeaffine.console.core.Consoles;
import com.codeaffine.gonsole.GitConsoleFactory;
import com.codeaffine.gonsole.internal.GitConsoleConfigurer;


public class ShowInGitConsoleHandler extends AbstractHandler {

  public static final String COMMAND_ID = "com.codeaffine.gonsole.internal.command.ShowInGitConsole";

  @Override
  public Object execute( ExecutionEvent event ) {
    WorkbenchState workbenchState = new WorkbenchState( event );
    openConsoleView( workbenchState );
    openConsole( workbenchState );
    return null;
  }

  private static void openConsoleView( WorkbenchState workbenchState ) {
    new ConsoleViewOpener( workbenchState.getActivePart().getSite().getPage() ).open();
  }

  private static void openConsole( WorkbenchState workbenchState ) {
    File initialRepositoryLocation = getInitialRepositoryLocation( workbenchState );
    Optional<Console> matchingConsole = Stream.of( Consoles.listConsoles() )
      .filter( console -> console.getConsoleConfigurer() instanceof GitConsoleConfigurer )
      .filter( console -> consoleShowsRepository( console, initialRepositoryLocation )  )
      .findFirst();
    if( matchingConsole.isPresent() ) {
      matchingConsole.get().activate();
    } else {
      new GitConsoleFactory( initialRepositoryLocation ).openConsole();
    }
  }

  private static boolean consoleShowsRepository( Console console, File repositoryLocation ) {
    GitConsoleConfigurer configurer = ( GitConsoleConfigurer )console.getConsoleConfigurer();
    File consoleRepositoryLocation = configurer.getRepositoryProvider().getCurrentRepositoryLocation();
    return Objects.equals( consoleRepositoryLocation, repositoryLocation );
  }

  private static File getInitialRepositoryLocation( WorkbenchState workbenchState ) {
    return new RepositoryLocationComputer( workbenchState ).compute();
  }
}
