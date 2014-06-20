package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;

public class ProposalComputer {

  public ICompletionProposal compute( String prefix, int start, int length, String proposal, Image image ) {
    String replacement = proposal.substring( prefix.length() );
    int cursorPosition = replacement.length();
    return new CompletionProposal( replacement, start, length, cursorPosition, image, proposal, null, null );
  }
}