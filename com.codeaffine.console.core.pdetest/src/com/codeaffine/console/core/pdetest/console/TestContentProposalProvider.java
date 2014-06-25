package com.codeaffine.console.core.pdetest.console;

import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMANDS;
import static com.google.common.collect.Iterables.toArray;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.pdetest.TestImageDescriptor;

class TestContentProposalProvider implements ContentProposalProvider {

  @Override
  public ImageDescriptor getImageDescriptor() {
    return new TestImageDescriptor();
  }

  @Override
  public String[] getContentProposals() {
    return toArray( COMMANDS, String.class );
  }
}