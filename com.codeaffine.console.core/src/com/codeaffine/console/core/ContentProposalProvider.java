package com.codeaffine.console.core;

import org.eclipse.jface.bindings.keys.KeyStroke;


public interface ContentProposalProvider {
  Proposal[] getContentProposals();
  KeyStroke getActivationKeySequence();
}
