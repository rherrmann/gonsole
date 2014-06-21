package com.codeaffine.console.core.internal.contentassist;

import java.util.Comparator;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

class ProposalComparator implements Comparator<ICompletionProposal> {

  @Override
  public int compare( ICompletionProposal proposal1, ICompletionProposal proposal2 ) {
    return proposal1.getDisplayString().compareTo( proposal2.getDisplayString() );
  }
}