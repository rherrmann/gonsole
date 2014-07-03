package com.codeaffine.gonsole.internal.contentassist;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.gonsole.internal.activator.IconRegistry;

public class ControlCommandContentProposalProvider implements ContentProposalProvider {

  static final Proposal[] PROPOSALS = new Proposal[] { new Proposal( "use", "ToDo" ) };

  @Override
  public Proposal[] getContentProposals() {
    return PROPOSALS;
  }

  @Override
  public ImageDescriptor getImageDescriptor() {
    return new IconRegistry().getDescriptor( IconRegistry.CONTROL_PROPOSAL );
  }
}