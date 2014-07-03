package com.codeaffine.console.core.pdetest.console;

import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMANDS;
import static com.google.common.collect.Iterables.toArray;

import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.pdetest.TestImageDescriptor;
import com.google.common.collect.Sets;

class TestContentProposalProvider implements ContentProposalProvider {

  @Override
  public ImageDescriptor getImageDescriptor() {
    return new TestImageDescriptor();
  }

  @Override
  public Proposal[] getContentProposals() {
    Collection<Proposal> result = Sets.newHashSet();
    for( String command : COMMANDS ) {
      result.add( new Proposal( command, command + " info" ) );
    }
    return toArray( result, Proposal.class );
  }
}