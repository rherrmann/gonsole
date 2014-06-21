package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

class TestAction extends Action {

  static final String COMMAND_ID = ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS;

  TestAction() {
    setActionDefinitionId( COMMAND_ID );
  }

  private boolean executed;

  @Override
  public void run() {
    executed = true;
  }


  boolean isExecuted() {
    return executed;
  }
}