package com.codeaffine.console.core;


public interface ContentProposalProvider {
  Proposal[] getContentProposals();
  String getActivationKeySequence();
}
