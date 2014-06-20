package com.codeaffine.console.core.pdetest.console;

import static com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter.COMMANDS;
import static com.google.common.collect.Iterables.toArray;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ContentProposalProvider;

class TestContentProposalProvider implements ContentProposalProvider {

  @Override
  public ImageDescriptor getImageDescriptor() {
    return TestConsoleDefinition.IMAGE;
  }

  @Override
  public String[] getContentProposals() {
    return toArray( COMMANDS, String.class );
  }
}