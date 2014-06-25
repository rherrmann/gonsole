package com.codeaffine.console.calculator.internal;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ContentProposalProvider;

class CalculatorContentProposalProvider implements ContentProposalProvider {

  @Override
  public ImageDescriptor getImageDescriptor() {
    return null;
  }

  @Override
  public String[] getContentProposals() {
    return new String[] { CalculatorConsoleCommandInterpreter.SUM };
  }
}