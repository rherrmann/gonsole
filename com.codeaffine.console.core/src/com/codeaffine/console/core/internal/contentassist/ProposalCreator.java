package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.internal.resource.ResourceRegistry;

class ProposalCreator {

  private final ResourceRegistry imageRegistry;

  ProposalCreator( Display display ) {
    this.imageRegistry = new ResourceRegistry( display );
  }

  ICompletionProposal create( String prefix, Proposal proposal, int start, int length ) {
    ICompletionProposal result = null;
    if( proposal.getText().startsWith( prefix ) ) {
      result = doCreate( prefix, proposal, start, length );
    }
    return result;
  }

  private ICompletionProposal doCreate( String prefix, Proposal proposal, int start, int length ) {
    String text = proposal.getText();
    String replacement = text.substring( prefix.length() );
    int cursorPosition = replacement.length();
    Image image = imageRegistry.getImage( proposal.getImageDescriptor() );
    String info = proposal.getInfo();
    return new CompletionProposal( replacement, start, length, cursorPosition, image, text, null, info );
  }

  void dispose() {
    imageRegistry.dispose();
  }
}