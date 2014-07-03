package com.codeaffine.console.core.internal.contentassist;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Lists.newLinkedList;
import static java.util.Collections.sort;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.internal.resource.ResourceRegistry;

public class ProposalCalculator {

  private final ContentProposalProvider[] proposalProviders;
  private final ProposalCreator proposalCreator;
  private final ResourceRegistry imageRegistry;

  public ProposalCalculator( ContentProposalProvider ... proposalProviders ) {
    this.proposalCreator = new ProposalCreator();
    this.imageRegistry = new ResourceRegistry();
    this.proposalProviders = proposalProviders;
  }

  public void dispose() {
    imageRegistry.dispose();
  }

  public ICompletionProposal[] calculate( String prefix, int start, int length ) {
    List<ICompletionProposal> result = newLinkedList();
    for( ContentProposalProvider proposalProvider : proposalProviders ) {
      result.addAll( calculate( proposalProvider, prefix, start, length ) );
    }
    sort( result, new ProposalComparator() );
    return toArray( result, ICompletionProposal.class );
  }

  private List<ICompletionProposal> calculate( ContentProposalProvider provider, String prefix, int start, int len ) {
    List<ICompletionProposal> result = newLinkedList();
    Image image = imageRegistry.getImage( provider.getImageDescriptor() );
    for( Proposal proposal : provider.getContentProposals() ) {
      ICompletionProposal completionProposal = proposalCreator.create( prefix, proposal, start, len, image );
      if( completionProposal != null ) {
        result.add( completionProposal );
      }
    }
    return result;
  }
}