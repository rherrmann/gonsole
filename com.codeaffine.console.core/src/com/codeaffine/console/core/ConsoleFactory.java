package com.codeaffine.console.core;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;

import com.codeaffine.console.core.internal.GenericConsole;

public abstract class ConsoleFactory implements IConsoleFactory {

  private final IConsoleManager consoleManager;

  public ConsoleFactory() {
    this( ConsolePlugin.getDefault().getConsoleManager() );
  }

  ConsoleFactory( IConsoleManager consoleManager ) {
    this.consoleManager = consoleManager;
  }

  @Override
  public void openConsole() {
    consoleManager.showConsoleView( createConsole() );
  }

  protected abstract ConsoleConfigurer getConsoleConfigurer();

  private GenericConsole createConsole() {
    GenericConsole result = new GenericConsole( getConsoleConfigurer() );
    consoleManager.addConsoles( new IConsole[] { result } );
    return result;
  }
}