package com.codeaffine.console.core.internal.contentassist;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.mockito.ArgumentCaptor;

class ValueCaptor {

  private final ICompletionProposal actual;

  private ArgumentCaptor<Integer> offsetCaptor;
  private ArgumentCaptor<Integer> lengthCaptor;
  private ArgumentCaptor<String> replacementCaptor;

  ValueCaptor( ICompletionProposal actual ) {
    this.actual = actual;
  }

  void captureReplacementValues() {
    IDocument document = mock( IDocument.class );
    actual.apply( document );
    offsetCaptor = forClass( Integer.class );
    lengthCaptor = forClass( Integer.class );
    replacementCaptor = forClass( String.class );
    try {
      verify( document ).replace( offsetCaptor.capture(), lengthCaptor.capture(), replacementCaptor.capture() );
    } catch( BadLocationException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  int getOffset() {
    return offsetCaptor.getValue().intValue();
  }

  int getLength() {
    return lengthCaptor.getValue().intValue();
  }

  String getReplacement() {
    return replacementCaptor.getValue();
  }
}