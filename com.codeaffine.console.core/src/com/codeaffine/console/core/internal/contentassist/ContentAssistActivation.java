package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

class ContentAssistActivation implements FocusListener {

  private final ActionActivation actionActivation;
  private final Action action;

  ContentAssistActivation( ContentAssistant contentAssistant, TextViewer textViewer ) {
    this.action = new ContentAssistAction( contentAssistant, new DocumentHolder( textViewer ) );
    this.actionActivation = new ActionActivation();
  }

  @Override
  public void focusGained( FocusEvent event ) {
    actionActivation.activate( action );
  }

  @Override
  public void focusLost( FocusEvent event ) {
    actionActivation.deactivate();
  }
}