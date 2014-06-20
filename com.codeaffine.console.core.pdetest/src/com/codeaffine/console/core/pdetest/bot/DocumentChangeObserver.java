package com.codeaffine.console.core.pdetest.bot;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

class DocumentChangeObserver implements IDocumentListener {

  private boolean changed;

  @Override
  public void documentChanged( DocumentEvent event ) {
    changed = true;
  }

  @Override
  public void documentAboutToBeChanged( DocumentEvent event ) {
  }

  boolean isChanged() {
    return changed;
  }
}