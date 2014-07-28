package com.codeaffine.gonsole.internal.contentassist;

import static com.codeaffine.gonsole.internal.activator.IconRegistry.GIT_PROPOSAL;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newLinkedList;
import static java.util.Arrays.asList;

import java.io.File;
import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jgit.pgm.CommandCatalog;
import org.eclipse.jgit.pgm.CommandRef;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.interpreter.AliasConfig;
import com.codeaffine.gonsole.internal.interpreter.CommandLineParser;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.google.common.base.Function;

public class GitCommandContentProposalProvider implements ContentProposalProvider {

  private final CompositeRepositoryProvider repositoryProvider;

  public GitCommandContentProposalProvider( CompositeRepositoryProvider repositoryProvider ) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public Proposal[] getContentProposals() {
    return toArray( concat( getCommandProposals(), getAliasProposals() ), Proposal.class );
  }

  @Override
  public String getActivationKeySequence() {
    return new KeyBinding().getContentAssistKeyBinding();
  }

  private static Iterable<Proposal> getCommandProposals() {
    return transform( asList( CommandCatalog.common() ), new CommandRefToProposalTransform() );
  }

  private Iterable<Proposal> getAliasProposals() {
    Collection<Proposal> result = newLinkedList();
    File currentRepositoryLocation = repositoryProvider.getCurrentRepositoryLocation();
    if ( currentRepositoryLocation != null ) {
      AliasConfig aliasConfig = new AliasConfig( currentRepositoryLocation );
      String[] aliases = aliasConfig.getAliases();
      for( String alias : aliases ) {
        String info = "Alias for: " + aliasConfig.getCommand( alias );
        result.add( createProposal( alias, info ) );
      }
    }
    return result;
  }

  private static Proposal createProposal( String text, String info ) {
    ImageDescriptor imageDescriptor = new IconRegistry().getDescriptor( GIT_PROPOSAL );
    return new Proposal( text, text, info, imageDescriptor );
  }

  private static class CommandRefToProposalTransform implements Function<CommandRef, Proposal> {
    @Override
    public Proposal apply( CommandRef input ) {
      String additionalInfo = new CommandLineParser().getUsage( input.getName() );
      return createProposal( input.getName(), additionalInfo );
    }
  }
}