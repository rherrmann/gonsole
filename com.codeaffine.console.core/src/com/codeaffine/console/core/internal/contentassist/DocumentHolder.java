package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.PartitionType.INPUT;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitionerExtension;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextViewer;

public class DocumentHolder {

  private final TextViewer textViewer;

  DocumentHolder( TextViewer textViewer ) {
    this.textViewer = textViewer;
  }

  public boolean isCaretAtDocumentEnd() {
    return getCaretOffset() == getDocument().getLength();
  }

  boolean hasInputPartionAtCaretOffset() {
    return INPUT.equals( getPartition().getType() );
  }

  boolean hasCorrectPartitioner() {
    return getDocument().getDocumentPartitioner() instanceof IDocumentPartitionerExtension;
  }

  void createInputPartitionAtCaret() {
    DocumentEvent event = new DocumentEvent( getDocument(), getCaretOffset(), 0, "" );
    ( ( IDocumentPartitionerExtension )getDocument().getDocumentPartitioner() ).documentChanged2( event );
  }

  private ITypedRegion getPartition() {
    ITypedRegion result = null;
    try {
      result = getDocument().getPartition( getCaretOffset() );
    } catch( BadLocationException shoultNotHappen ) {
      throw new IllegalStateException( shoultNotHappen );
    }
    return result;
  }

  private int getCaretOffset() {
    return textViewer.getTextWidget().getCaretOffset();
  }

  private IDocument getDocument() {
    return textViewer.getDocument();
  }
}