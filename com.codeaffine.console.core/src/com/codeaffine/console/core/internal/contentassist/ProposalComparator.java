package com.codeaffine.console.core.internal.contentassist;

import java.util.Comparator;

import com.codeaffine.console.core.Proposal;

class ProposalComparator implements Comparator<Proposal> {

  @SuppressWarnings("unchecked")
  @Override
  public int compare( Proposal proposal1, Proposal proposal2 ) {
    Comparable<Object> sortKey1 = ( Comparable<Object> )proposal1.getSortKey();
    Comparable<Object> sortKey2 = ( Comparable<Object> )proposal2.getSortKey();
    return sortKey1.compareTo( sortKey2 );
  }
}