package com.codeaffine.console.core;

import org.eclipse.jface.resource.ImageDescriptor;

public interface ContentProposalProvider {
  Proposal[] getContentProposals();
  ImageDescriptor getImageDescriptor();
  String getActivationKeySequence();
}
