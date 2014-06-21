package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;

class ProposalCreator {

  ICompletionProposal create( String prefix, String proposal, int start, int length, Image image   ) {
    ICompletionProposal result = null;
    if( proposal.startsWith( prefix ) ) {
      result = doCreate( prefix, proposal, start, length, image );
    }
    return result;
  }

  private static ICompletionProposal doCreate( String prefix, String proposal, int start, int length, Image image  ) {
    String replacement = proposal.substring( prefix.length() );
    int cursorPosition = replacement.length();
    return new CompletionProposal( replacement, start, length, cursorPosition, image, proposal, null, null );
  }
}