package com.codeaffine.gonsole;

import org.eclipse.jface.resource.ImageDescriptor;

public interface ContentProposalProvider {
  String[] getContentProposals();
  ImageDescriptor getImageDescriptor();
}
