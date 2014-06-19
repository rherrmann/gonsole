package com.codeaffine.gonsole.internal.gitconsole;

import static com.google.common.collect.Iterables.tryFind;
import static java.util.Arrays.asList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;

import com.codeaffine.gonsole.internal.Console;
import com.google.common.base.Predicate;

public class GitConsoleFactory implements IConsoleFactory {

  private static final Predicate<IConsole> IS_GIT_CONSOLE_PREDICATE = new Predicate<IConsole>() {
    @Override
    public boolean apply( IConsole input ) {
      return input instanceof Console;
    }
  };

  private final IConsoleManager consoleManager;

  public GitConsoleFactory() {
    this( ConsolePlugin.getDefault().getConsoleManager() );
  }

  GitConsoleFactory( IConsoleManager consoleManager ) {
    this.consoleManager = consoleManager;
  }

  @Override
  public void openConsole() {
    consoleManager.showConsoleView( getConsole() );
  }

  private IConsole getConsole() {
    IConsole result = getExistingConsole();
    if( result == null ) {
      result = createConsole();
    }
    return result;
  }

  private IConsole getExistingConsole() {
    return tryFind( asList( consoleManager.getConsoles() ), IS_GIT_CONSOLE_PREDICATE ).orNull();
  }

  private Console createConsole() {
    Console result = new Console( new GitConsoleDefinition( Display.getCurrent() ) );
    consoleManager.addConsoles( new IConsole[] { result } );
    return result;
  }
}