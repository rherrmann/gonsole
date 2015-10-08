package com.codeaffine.console.core.internal;

import static org.eclipse.ui.console.IConsoleConstants.LAUNCH_GROUP;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.console.actions.CloseConsoleAction;
import org.eclipse.ui.part.IPageBookViewPage;

public class GenericConsolePageParticipant implements IConsolePageParticipant {

  public static final String ID
    = "com.codeaffine.console.core.internal.GenericConsolePageParticipant";

  @Override
  public void init( IPageBookViewPage page, IConsole console ) {
    contributeToolAction( page, LAUNCH_GROUP, createCloseAction( console ) );
  }

  @Override
  public void dispose() {
  }

  @Override
  public void activated() {
  }

  @Override
  public void deactivated() {
  }

  @Override
  @SuppressWarnings({
    "rawtypes",
    "unchecked"
  })
  public Object getAdapter( Class adapter ) {
    return null;
  }

  private static void contributeToolAction( IPageBookViewPage page, String group, IAction action ) {
    IActionBars actionBars = page.getSite().getActionBars();
    actionBars.getToolBarManager().appendToGroup( group, action );
  }

  private static CloseConsoleAction createCloseAction( IConsole console ) {
    CloseConsoleAction result = new CloseConsoleAction( console );
    result.setText( "Close" );
    return result;
  }
}
