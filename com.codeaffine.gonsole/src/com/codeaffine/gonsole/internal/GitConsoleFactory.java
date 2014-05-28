package com.codeaffine.gonsole.internal;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;


public class GitConsoleFactory implements IConsoleFactory {

  @Override
  public void openConsole() {
    IConsole console = new GitConsole();
    ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[] { console } );
  }

}
