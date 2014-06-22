package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitionerExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

public class Editor implements FocusListener {

  private final ContentAssistActivation contentAssistActivation;
  private final ConsoleContentAssist contentAssist;
  private final ITextViewer viewer;

  Editor( ITextViewer viewer, ConsoleContentAssist contentAssist ) {
    this( viewer, contentAssist, ( ContentAssistActivation )null );
  }

  public Editor( ITextViewer viewer, ConsoleContentAssist contentAssist, ContentAssistActivation activation ) {
    this.contentAssistActivation = activation == null ? new ContentAssistActivation( this ) : activation;
    this.contentAssist = contentAssist;
    this.viewer = viewer;
  }

  @Override
  public void focusGained( FocusEvent e ) {
    contentAssistActivation.activate();
  }

  @Override
  public void focusLost( FocusEvent e ) {
    contentAssistActivation.deactivate();
  }

  int getCaretOffset() {
    return viewer.getTextWidget().getCaretOffset();
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
    ( ( IDocumentPartitionerExtension )getDocument().getDocumentPartitioner() ).documentChanged2( event );
  }

  void showPossibleCompletions() {
    contentAssist.showPossibleCompletions();
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

  private String getText( int partitionOffset, int length ) {
    try {
      return getDocument().get( partitionOffset, length );
    } catch( BadLocationException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private ITypedRegion getPartition( int caretOffset ) {
    try {
      return getDocument().getPartition( caretOffset );
    } catch( BadLocationException shoultNotHappen ) {
      throw new IllegalStateException( shoultNotHappen );
    }
  }

  private IDocument getDocument() {
    return viewer.getDocument();
  }
}