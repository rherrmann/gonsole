package com.codeaffine.gonsole.internal;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.gonsole.internal.activator.IconRegistry;


public class ControlCommandContentProposalProvider implements ContentProposalProvider {

  @Override
  public String[] getContentProposals() {
    return new String[] { "use" };
  }

  @Override
  public ImageDescriptor getImageDescriptor() {
    return new IconRegistry().getDescriptor( IconRegistry.CONTROL_PROPOSAL );
  }
}
