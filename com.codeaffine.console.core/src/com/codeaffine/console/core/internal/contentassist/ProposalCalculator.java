package com.codeaffine.console.core.internal.contentassist;

import static java.util.Collections.sort;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.internal.ConsoleEditor;

public class ProposalCalculator {

  private final ContentProposalProvider[] proposalProviders;
  private final ProposalCreator proposalCreator;
  private final ConsoleEditor consoleEditor;

  public ProposalCalculator( ConsoleEditor consoleEditor, ContentProposalProvider ... proposalProviders ) {
    this.consoleEditor = consoleEditor;
    this.proposalCreator = new ProposalCreator( consoleEditor );
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
    List<Proposal> result = new LinkedList<>();
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
    return proposals.stream()
      .map( proposal -> proposalCreator.create( prefix, proposal, start, length ) )
      .filter( Objects::nonNull )
      .toArray( ICompletionProposal[]::new );
  }

  private boolean matchesActivationKeySequence( ContentProposalProvider proposalProvider ) {
    KeyStroke currentActivationKeySequence = consoleEditor.getActivationKeyStroke();
    KeyStroke proposalActivationKeySequence = proposalProvider.getActivationKeySequence();
    return currentActivationKeySequence.equals( proposalActivationKeySequence );
  }
}