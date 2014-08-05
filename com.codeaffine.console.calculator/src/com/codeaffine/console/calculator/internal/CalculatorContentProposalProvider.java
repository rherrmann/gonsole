package com.codeaffine.console.calculator.internal;

import static com.codeaffine.console.calculator.internal.CalculatorConsoleCommandInterpreter.SUM;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;

class CalculatorContentProposalProvider implements ContentProposalProvider {

  private static final String INFO = "Sums up two integer values.";

  @Override
  public Proposal[] getContentProposals() {
    return new Proposal[] { new Proposal( SUM, SUM, INFO, null ) };
  }

  @Override
  public KeyStroke getActivationKeySequence() {
    return KeyStroke.getInstance( SWT.MOD1, SWT.SPACE );
  }
}