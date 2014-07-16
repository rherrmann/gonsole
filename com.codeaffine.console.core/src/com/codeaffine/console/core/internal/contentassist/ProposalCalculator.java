package com.codeaffine.console.core.internal.contentassist;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Lists.newLinkedList;
import static java.util.Collections.sort;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;

public class ProposalCalculator {

  private final ContentProposalProvider[] proposalProviders;
  private final ProposalCreator proposalCreator;
  private final Editor editor;

  public ProposalCalculator( Editor editor, ContentProposalProvider ... proposalProviders ) {
    this.editor = editor;
    this.proposalCreator = new ProposalCreator( editor );
    this.proposalProviders = proposalProviders;
  }

  public void dispose() {
    proposalCreator.dispose();
  }

  public ICompletionProposal[] calculate( String prefix, int start, int length ) {
    List<Proposal> proposals = collectProposals();
    sortProposals( proposals );
    return calculateProposals( prefix, start, length, proposals );
  }

  private List<Proposal> collectProposals() {
    List<Proposal> result = newLinkedList();
    for( ContentProposalProvider proposalProvider : proposalProviders ) {
      for( Proposal proposal : proposalProvider.getContentProposals() ) {
        if( matchesActivationKeySequence( proposalProvider ) ) {
          result.add( proposal );
        }
      }
    }
    return result;
  }

  private static void sortProposals( List<Proposal> proposals ) {
    sort( proposals, new ProposalComparator() );
  }

  private ICompletionProposal[] calculateProposals( String prefix,
                                                    int start,
                                                    int length,
                                                    List<Proposal> proposals )
  {
    List<ICompletionProposal> result = newLinkedList();
    for( Proposal proposal : proposals ) {
      ICompletionProposal completionProposal = proposalCreator.create( prefix, proposal, start, length );
      if( completionProposal != null ) {
        result.add( completionProposal );
      }
    }
    return toArray( result, ICompletionProposal.class );
  }

  private boolean matchesActivationKeySequence( ContentProposalProvider proposalProvider ) {
    String currentActivationKeySequence = editor.getActivationKeySequence();
    String proposalActivationKeySequence = proposalProvider.getActivationKeySequence();
    return currentActivationKeySequence.equalsIgnoreCase( proposalActivationKeySequence );
  }
}