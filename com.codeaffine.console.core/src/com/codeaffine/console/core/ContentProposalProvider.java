package com.codeaffine.console.core;

import org.eclipse.jface.resource.ImageDescriptor;

public interface ContentProposalProvider {
  String[] getContentProposals();
  ImageDescriptor getImageDescriptor();
}
