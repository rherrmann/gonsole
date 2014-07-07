package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitionerExtension;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.widgets.Display;

class Editor {

  private final TextViewer textViewer;
  private final ActionKeyBindingManager actionKeyBindingManager;

  Editor( TextViewer textViewer ) {
    this.textViewer = textViewer;
    this.actionKeyBindingManager = new ActionKeyBindingManager();
    this.actionKeyBindingManager.activateFor( textViewer );
  }

  TextViewer getTextViewer() {
    return textViewer;
  }

  void addAction( String keySequence, IAction action ) {
    actionKeyBindingManager.addKeyBinding( keySequence, action );
  }

  String getActivationKeySequence() {
    return actionKeyBindingManager.getActiveKeySequence();
  }

  int getCaretOffset() {
    return textViewer.getTextWidget().getCaretOffset();
  }

  int getDocumentLength() {
    return getDocument().getLength();
  }

  String getPartitionType() {
    return getPartition( getCaretOffset() ).getType();
  }

  boolean isDocumentChangeAllowed() {
    return getDocument().getDocumentPartitioner() instanceof IDocumentPartitionerExtension;
  }

  void fireDocumentChange() {
    DocumentEvent event = new DocumentEvent( getDocument(), getCaretOffset(), 0, "" );
    getDocumentPartitioner().documentChanged2( event );
  }

  String computePrefix( int offset ) {
    String result = "";
    if( offset >= 0 && offset <= getDocument().getLength() ) {
      ITypedRegion region = getPartition( offset );
      int partitionOffset = region.getOffset();
      int length = offset - partitionOffset;
      result = getText( partitionOffset, length );
    }
    return result;
  }

  boolean isCaretInLastInputPartition() {
    boolean result = false;
    try {
      ITypedRegion currentPartition = getDocument().getPartition( getCaretOffset() );
      ITypedRegion lastPartition = getDocument().getPartition( getDocumentLength() );
      result = currentPartition == lastPartition;
    } catch( BadLocationException ignore ) {
    }
    return result;
  }

  private String getText( int partitionOffset, int length ) {
    try {
      return getDocument().get( partitionOffset, length );
    } catch( BadLocationException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private IDocumentPartitionerExtension getDocumentPartitioner() {
    return ( IDocumentPartitionerExtension )getDocument().getDocumentPartitioner();
  }

  private ITypedRegion getPartition( int caretOffset ) {
    try {
      return getDocument().getPartition( caretOffset );
    } catch( BadLocationException shoultNotHappen ) {
      throw new IllegalStateException( shoultNotHappen );
    }
  }

  private IDocument getDocument() {
    return textViewer.getDocument();
  }

  Display getDisplay() {
    return textViewer.getControl().getDisplay();
  }
}
