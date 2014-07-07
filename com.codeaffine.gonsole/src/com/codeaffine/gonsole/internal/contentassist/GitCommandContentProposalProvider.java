package com.codeaffine.gonsole.internal.contentassist;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jgit.pgm.CommandCatalog;
import org.eclipse.jgit.pgm.CommandRef;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.interpreter.CommandLineParser;
import com.google.common.base.Function;

public class GitCommandContentProposalProvider implements ContentProposalProvider {

  @Override
  public Proposal[] getContentProposals() {
    return toArray( transform( asList( CommandCatalog.common() ), byProposal() ), Proposal.class );
  }

  @Override
  public ImageDescriptor getImageDescriptor() {
    return new IconRegistry().getDescriptor( IconRegistry.GIT_PROPOSAL );
  }

  private static Function<CommandRef, Proposal> byProposal() {
    return new Function<CommandRef,Proposal>() {
      @Override
      public Proposal apply( CommandRef input ) {
        String additionalInfo = new CommandLineParser().getUsage( input.getName() );
        return new Proposal( input.getName(), additionalInfo );
      }
    };
  }

  @Override
  public String getActivationKeySequence() {
    return "Ctrl+Space";
  }
}