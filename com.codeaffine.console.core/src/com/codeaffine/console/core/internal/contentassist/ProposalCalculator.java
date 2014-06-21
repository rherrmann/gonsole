package com.codeaffine.console.core.internal.contentassist;

import static com.google.common.collect.Lists.newLinkedList;
import static java.util.Collections.sort;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.internal.resource.ResourceRegistry;

class ProposalCalculator {

  private final ProposalCreator proposalCreator;
  private final ResourceRegistry imageRegistry;

  ProposalCalculator() {
    this.imageRegistry = new ResourceRegistry();
    this.proposalCreator = new ProposalCreator();
  }

  List<ICompletionProposal> calculate( ContentProposalProvider[] providers , String prefix , int start , int length  ) {
    List<ICompletionProposal> result = newLinkedList();
    for( ContentProposalProvider proposalProvider : providers ) {
      result.addAll( calculate( proposalProvider, prefix, start, length ) );
    }
    sort( result, new ProposalComparator() );
    return result;
  }

  private List<ICompletionProposal> calculate(
    ContentProposalProvider proposalProvider, String prefix, int start, int length )
  {
    List<ICompletionProposal> result = newLinkedList();
    Image image = imageRegistry.getImage( proposalProvider.getImageDescriptor() );
    for( String proposal : proposalProvider.getContentProposals() ) {
      ICompletionProposal completionProposal = proposalCreator.create( prefix, proposal, start, length, image );
      if( completionProposal != null ) {
        result.add( completionProposal );
      }
    }
    return result;
  }

  public void dispose() {
    imageRegistry.dispose();
  }
}