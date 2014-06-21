package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;

class ActionActivation {

  private final IHandlerService handlerService;

  private IHandlerActivation activation;

  ActionActivation() {
    this.handlerService = ( IHandlerService )PlatformUI.getWorkbench().getService( IHandlerService.class );
  }

  void activate( Action action ) {
    if( activation == null ) {
      String actionId = action.getActionDefinitionId();
      ActionHandler handler = new ActionHandler( action );
      activation = handlerService.activateHandler( actionId, handler, null );
    }
  }

  void deactivate() {
    if( activation != null ) {
      handlerService.deactivateHandler( activation );
      activation = null;
    }
  }
}