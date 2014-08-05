package com.codeaffine.console.core.internal;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitionerExtension;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.widgets.Display;

import com.codeaffine.console.core.internal.contentassist.ActionKeyBindingManager;

public class ConsoleEditor {

  private final TextViewer textViewer;
  private final ActionKeyBindingManager actionKeyBindingManager;
  private final CaretPositionUpdater caretPositionUpdater;

  public ConsoleEditor( TextViewer textViewer ) {
    this.textViewer = textViewer;
    this.actionKeyBindingManager = new ActionKeyBindingManager();
    this.actionKeyBindingManager.activateFor( textViewer );
    this.caretPositionUpdater = new CaretPositionUpdater( textViewer );
    this.caretPositionUpdater.install();
  }

  public TextViewer getTextViewer() {
    return textViewer;
  }

  public Display getDisplay() {
    return textViewer.getControl().getDisplay();
  }

  public void addAction( KeyStroke keyStroke, IAction action ) {
    actionKeyBindingManager.addKeyBinding( keyStroke, action );
  }

  public KeyStroke getActivationKeyStroke() {
    return actionKeyBindingManager.getActiveKeySequence();
  }

  public int getCaretOffset() {
    return textViewer.getTextWidget().getCaretOffset();
  }

  public int getDocumentLength() {
    return getDocument().getLength();
  }

  public String getPartitionType() {
    return getPartition( getCaretOffset() ).getType();
  }

  public boolean isDocumentChangeAllowed() {
    return getDocument().getDocumentPartitioner() instanceof IDocumentPartitionerExtension;
  }

  public void fireDocumentChange() {
    DocumentEvent event = new DocumentEvent( getDocument(), getCaretOffset(), 0, "" );
    getDocumentPartitioner().documentChanged2( event );
  }

  public String computePrefix( int offset ) {
    String result = "";
    if( offset >= 0 && offset <= getDocument().getLength() ) {
      ITypedRegion region = getPartition( offset );
      int partitionOffset = region.getOffset();
      int length = offset - partitionOffset;
      result = getText( partitionOffset, length );
    }
    return result;
  }

  public boolean isCaretInLastInputPartition() {
    boolean result = false;
    try {
      ITypedRegion currentPartition = getDocument().getPartition( getCaretOffset() );
      ITypedRegion lastPartition = getDocument().getPartition( getDocumentLength() );
      result = currentPartition == lastPartition;
    } catch( BadLocationException ignore ) {
    }
    return result;
  }

  public void enabledCaretPositionUpdater() {
    caretPositionUpdater.enable();
  }

  public void disableCaretPositionUpdater() {
    caretPositionUpdater.disable();
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
}
