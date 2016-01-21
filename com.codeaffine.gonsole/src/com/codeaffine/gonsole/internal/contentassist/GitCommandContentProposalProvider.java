package com.codeaffine.gonsole.internal.contentassist;

import static com.codeaffine.gonsole.internal.activator.IconRegistry.GIT_PROPOSAL;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jgit.pgm.CommandCatalog;
import org.eclipse.jgit.pgm.CommandRef;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.interpreter.AliasConfig;
import com.codeaffine.gonsole.internal.interpreter.CommandLineParser;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

public class GitCommandContentProposalProvider implements ContentProposalProvider {

  private final CompositeRepositoryProvider repositoryProvider;

  public GitCommandContentProposalProvider( CompositeRepositoryProvider repositoryProvider ) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public Proposal[] getContentProposals() {
    return Stream.concat( getCommandProposals().stream(), getAliasProposals().stream() )
      .toArray( Proposal[]::new );
  }

  @Override
  public KeyStroke getActivationKeySequence() {
    return new KeyBinding().getContentAssistKeyBinding();
  }

  private static Collection<Proposal> getCommandProposals() {
    return Stream.of( CommandCatalog.common() )
      .map( GitCommandContentProposalProvider::toProposal )
      .collect( Collectors.toList() );
  }

  private Collection<Proposal> getAliasProposals() {
    Collection<Proposal> result = new LinkedList<>();
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

  private static Proposal toProposal( CommandRef input ) {
    String additionalInfo = new CommandLineParser().getUsage( input.getName() );
    return createProposal( input.getName(), additionalInfo );
  }

  private static Proposal createProposal( String text, String info ) {
    ImageDescriptor imageDescriptor = new IconRegistry().getDescriptor( GIT_PROPOSAL );
    return new Proposal( text, text, info, imageDescriptor );
  }
}