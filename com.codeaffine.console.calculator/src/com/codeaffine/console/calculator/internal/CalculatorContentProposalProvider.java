package com.codeaffine.console.calculator.internal;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;

class CalculatorContentProposalProvider implements ContentProposalProvider {

  private static final String INFO = "Sums up two integer values.";

  @Override
  public ImageDescriptor getImageDescriptor() {
    return null;
  }

  @Override
  public Proposal[] getContentProposals() {
    return new Proposal[] { new Proposal( CalculatorConsoleCommandInterpreter.SUM, INFO ) };
  }
}