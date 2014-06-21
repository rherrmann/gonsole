package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

class ContentAssistAction extends Action {

  private final ContentAssistant contentAssistant;
  private final DocumentHolder documentHolder;

  ContentAssistAction( ContentAssistant contentAssistant, DocumentHolder documentHolder ) {
    this.contentAssistant = contentAssistant;
    this.documentHolder = documentHolder;
    setActionDefinitionId( ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS );
    setEnabled( true );
  }

  @Override
  public void run() {
    ensureInputPartitionIfCaretIsAtPromptEnd();
    contentAssistant.showPossibleCompletions();
  }

  private void ensureInputPartitionIfCaretIsAtPromptEnd() {
    if( documentHolder.isCaretAtDocumentEnd()
        && !documentHolder.hasInputPartionAtCaretOffset()
        && documentHolder.hasCorrectPartitioner() )
    {
      documentHolder.createInputPartitionAtCaret();
    }
  }
}