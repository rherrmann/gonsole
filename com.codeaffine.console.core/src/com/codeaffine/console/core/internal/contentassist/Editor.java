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
    return getPartition().getType();
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

  private ITypedRegion getPartition() {
    ITypedRegion result = null;
    try {
      result = getDocument().getPartition( getCaretOffset() );
    } catch( BadLocationException shoultNotHappen ) {
      throw new IllegalStateException( shoultNotHappen );
    }
    return result;
  }

  private IDocument getDocument() {
    return viewer.getDocument();
  }
}