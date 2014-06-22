package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.PartitionType.INPUT;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

class ContentAssistAction extends Action {

  static final String ACTION_DEFINITION_ID = ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS;

  private final Editor editor;

  ContentAssistAction( Editor editor ) {
    this.editor = editor;
    setActionDefinitionId( ACTION_DEFINITION_ID );
    setEnabled( true );
  }

  @Override
  public void run() {
    ensureInputPartitionIfCaretAtEnd();
    showPossibleCompletions();
  }

  private void ensureInputPartitionIfCaretAtEnd() {
    if( mustChangePartitionType() ) {
      createInputPartition();
    }
  }

  private boolean mustChangePartitionType() {
    return    editor.getCaretOffset() == editor.getDocumentLength()
           && !INPUT.equals( editor.getPartitionType() )
           && editor.isDocumentChangeAllowed();
  }

  private void createInputPartition() {
    editor.fireDocumentChange();
  }

  private void showPossibleCompletions() {
    editor.showPossibleCompletions();
  }
}