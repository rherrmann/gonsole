package com.codeaffine.console.core;

import static com.google.common.collect.Iterables.tryFind;
import static java.util.Arrays.asList;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;

import com.codeaffine.console.core.internal.GenericConsole;
import com.google.common.base.Predicate;

public abstract class ConsoleFactory implements IConsoleFactory {

  private static final Predicate<IConsole> IS_CONSOLE_PREDICATE = new Predicate<IConsole>() {
    @Override
    public boolean apply( IConsole input ) {
      return input instanceof GenericConsole;
    }
  };

  private final IConsoleManager consoleManager;

  public ConsoleFactory() {
    this( ConsolePlugin.getDefault().getConsoleManager() );
  }

  ConsoleFactory( IConsoleManager consoleManager ) {
    this.consoleManager = consoleManager;
  }

  @Override
  public void openConsole() {
    consoleManager.showConsoleView( getConsole() );
  }

  protected abstract ConsoleConfigurer getConsoleConfigurer();

  private IConsole getConsole() {
    IConsole result = getExistingConsole();
    if( result == null ) {
      result = createConsole();
    }
    return result;
  }

  private IConsole getExistingConsole() {
    return tryFind( asList( consoleManager.getConsoles() ), IS_CONSOLE_PREDICATE ).orNull();
  }

  private GenericConsole createConsole() {
    GenericConsole result = new GenericConsole( getConsoleConfigurer() );
    consoleManager.addConsoles( new IConsole[] { result } );
    return result;
  }
}