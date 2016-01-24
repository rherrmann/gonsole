package com.codeaffine.gonsole.internal.contentassist;

import static com.codeaffine.gonsole.internal.activator.IconRegistry.GIT_PROPOSAL;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
  private final CommandLineParser commandLineParser;

  public GitCommandContentProposalProvider( CompositeRepositoryProvider repositoryProvider ) {
    this.repositoryProvider = repositoryProvider;
    this.commandLineParser = new CommandLineParser();
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

  private Collection<Proposal> getCommandProposals() {
    return new CommandRefCollector().collect().stream().map( this::toProposal ).collect( toList() );
  }

  private Collection<Proposal> getAliasProposals() {
    Collection<Proposal> result = Collections.emptyList();
    File currentRepositoryLocation = repositoryProvider.getCurrentRepositoryLocation();
    if( currentRepositoryLocation != null ) {
      AliasConfig aliasConfig = new AliasConfig( currentRepositoryLocation );
      result = Stream.of( aliasConfig.getAliases() )
        .map( alias -> createProposal( alias, "Alias for: " + aliasConfig.getCommand( alias ) ) )
        .collect( Collectors.toList() );
    }
    return result;
  }

  private Proposal toProposal( CommandRef commandRef ) {
    String additionalInfo = commandLineParser.getUsage( commandRef.getName() );
    return createProposal( commandRef.getName(), additionalInfo );
  }

  private static Proposal createProposal( String text, String info ) {
    ImageDescriptor imageDescriptor = new IconRegistry().getDescriptor( GIT_PROPOSAL );
    return new Proposal( text, text, info, imageDescriptor );
  }

  private static class CommandRefCollector {
    private final Set<CommandRef> commandRefs;

    CommandRefCollector() {
      commandRefs = new HashSet<>();
    }

    Collection<CommandRef> collect() {
      Collections.addAll( commandRefs, CommandCatalog.common() );
      addExtraCommandRef( "remote" );
      addExtraCommandRef( "blame" );
      addExtraCommandRef( "merge-base" );
      addExtraCommandRef( "rev-parse" );
      return commandRefs;
    }

    private void addExtraCommandRef( String commandName ) {
      CommandRef commandRef = CommandCatalog.get( commandName );
      if( commandRef != null ) {
        commandRefs.add( commandRef );
      }
    }
  }

}