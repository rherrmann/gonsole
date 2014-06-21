package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;

class ProposalPrefixComputer {

  String compute( ITextViewer viewer, int offset ) {
    String result = "";
    try {
      result = compute( viewer.getDocument(), offset );
    } catch( BadLocationException ignore ) {
    }
    return result;
  }

  private static String compute( IDocument document , int offset  ) throws BadLocationException {
    ITypedRegion region = document.getPartition( offset );
    int partitionOffset = region.getOffset();
    int length = offset - partitionOffset;
    return document.get( partitionOffset, length );
  }
}