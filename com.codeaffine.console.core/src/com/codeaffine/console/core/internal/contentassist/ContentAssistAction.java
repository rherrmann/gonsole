package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.action.Action;

class ContentAssistAction extends Action {

  private final ContentAssist contentAssist;

  ContentAssistAction( ContentAssist contentAssist ) {
    this.contentAssist = contentAssist;
  }

  @Override
  public void run() {
    contentAssist.showPossibleCompletions();
  }
}