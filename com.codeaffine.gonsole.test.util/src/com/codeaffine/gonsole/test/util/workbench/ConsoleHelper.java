package com.codeaffine.gonsole.test.util.workbench;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.gonsole.test.util.workbench.PartHelper.CONSOLE_VIEW_ID;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;


public class ConsoleHelper {

  private final IWorkbench workbench;
  private final IConsoleManager consoleManager;
  private final PartHelper partHelper;

  public ConsoleHelper() {
    workbench = PlatformUI.getWorkbench();
    consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    partHelper = new PartHelper();
  }

  public IConsoleView getConsoleView() {
    long start = System.currentTimeMillis();
    IViewPart result = partHelper.findView( CONSOLE_VIEW_ID );
    while( result == null && System.currentTimeMillis() - start < 5000 ) {
      flushPendingEvents();
      result = partHelper.findView( CONSOLE_VIEW_ID );
    }
    while( result != workbench.getActiveWorkbenchWindow().getActivePage().getActivePart() ) {
      flushPendingEvents();
    }
    return ( IConsoleView )result;
  }

  public IConsole[] getConsoles() {
    return consoleManager.getConsoles();
  }

  public void hideConsoleView() {
    consoleManager.removeConsoles( consoleManager.getConsoles() );
    partHelper.hideView( CONSOLE_VIEW_ID );
  }

}
