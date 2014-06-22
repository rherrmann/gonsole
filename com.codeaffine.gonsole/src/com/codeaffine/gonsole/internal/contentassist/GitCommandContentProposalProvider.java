package com.codeaffine.gonsole.internal.contentassist;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jgit.pgm.CommandCatalog;
import org.eclipse.jgit.pgm.CommandRef;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.google.common.base.Function;

public class GitCommandContentProposalProvider implements ContentProposalProvider {

  @Override
  public String[] getContentProposals() {
    return toArray( transform( asList( CommandCatalog.all() ), byName() ), String.class );
  }

  @Override
  public ImageDescriptor getImageDescriptor() {
    return new IconRegistry().getDescriptor( IconRegistry.GIT_PROPOSAL );
  }

  private static Function<CommandRef, String> byName() {
    return new Function<CommandRef,String>() {
      @Override
      public String apply( CommandRef input ) {
        return input.getName();
      }
    };
  }
}