package com.codeaffine.console.core.internal.contentassist;

import static com.google.common.base.Preconditions.checkState;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;

public class ContentAssistActivation {

  private final IHandlerService handlerService;
  private final Action action;

  private IHandlerActivation activation;

  ContentAssistActivation( Editor editor ) {
    this( new ContentAssistAction( editor ) );
  }

  ContentAssistActivation( Action action ) {
    this.action = action;
    this.handlerService = ( IHandlerService )PlatformUI.getWorkbench().getService( IHandlerService.class );
  }

  boolean isActive() {
    return activation != null;
  }

  public void activate() {
    checkState( !isActive(), "Action has already been activated." );

    String actionId = action.getActionDefinitionId();
    ActionHandler handler = new ActionHandler( action );
    activation = handlerService.activateHandler( actionId, handler, null );
  }

  public void deactivate() {
    checkState( isActive(), "Action has not been activated." );

    handlerService.deactivateHandler( activation );
    activation = null;
  }
}