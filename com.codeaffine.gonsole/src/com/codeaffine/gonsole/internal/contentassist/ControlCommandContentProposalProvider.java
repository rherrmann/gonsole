package com.codeaffine.gonsole.internal.contentassist;

import org.eclipse.jface.bindings.keys.KeyStroke;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.gonsole.internal.activator.IconRegistry;

public class ControlCommandContentProposalProvider implements ContentProposalProvider {

  private static final String USAGE
    = "use repository - Change the current repository\n\n"
    + "repository: Name or full path to the .git directory of the repository to use. "
    + "The name is taken from the last segment of the path to the repository (without .git). "
    + "E.g. the name of /path/to/repo/.git is 'repo'.";

  static final Proposal[] PROPOSALS = new Proposal[] {
    new Proposal( "use",
                  "use",
                  USAGE,
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