package com.codeaffine.gonsole.internal;

import org.eclipse.jface.resource.ImageDescriptor;

public interface ContentProposalProvider {

  String[] getContentProposals();
  ImageDescriptor getImageDescriptor();
}
