package com.codeaffine.gonsole.internal.contentassist;

import static com.codeaffine.gonsole.internal.interpreter.ControlCommands.HELP;
import static com.codeaffine.gonsole.internal.interpreter.ControlCommands.USAGE;
import static com.codeaffine.gonsole.internal.interpreter.ControlCommands.USE;

import org.eclipse.jface.bindings.keys.KeyStroke;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.gonsole.internal.activator.IconRegistry;

public class ControlCommandContentProposalProvider implements ContentProposalProvider {

  static final Proposal[] PROPOSALS = new Proposal[] {
    new Proposal( USE,
                  USE,
                  USAGE.get( USE ),
                  new IconRegistry().getDescriptor( IconRegistry.CONTROL_PROPOSAL ) ),
    new Proposal( HELP,
                  HELP,
                  USAGE.get( HELP ),
                  new IconRegistry().getDescriptor( IconRegistry.CONTROL_PROPOSAL ) )
  };

  @Override
  public Proposal[] getContentProposals() {
    return PROPOSALS;
  }

  @Override
  public KeyStroke getActivationKeySequence() {
    return new KeyBinding().getContentAssistKeyBinding();
  }
}