package com.codeaffine.console.core.pdetest.console;

import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMANDS;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.pdetest.TestImageDescriptor;

class TestContentProposalProvider implements ContentProposalProvider {

  @Override
  public Proposal[] getContentProposals() {
    return COMMANDS.stream()
      .map( command -> new Proposal( command, command, command + " info", new TestImageDescriptor() ) )
      .toArray( Proposal[]::new );
  }

  @Override
  public KeyStroke getActivationKeySequence() {
    return KeyStroke.getInstance( SWT.MOD1, SWT.SPACE );
  }
}