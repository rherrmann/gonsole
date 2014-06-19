package com.codeaffine.gonsole.internal.gitconsole.contentassist;

import static com.google.common.collect.Iterables.toArray;

import java.util.Arrays;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jgit.pgm.CommandCatalog;
import org.eclipse.jgit.pgm.CommandRef;

import com.codeaffine.gonsole.ContentProposalProvider;
import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class GitCommandContentProposalProvider implements ContentProposalProvider {

  @Override
  public String[] getContentProposals() {
    CommandRef[] commandRefs = CommandCatalog.all();
    Iterable<String> transform = Iterables.transform( Arrays.asList( commandRefs ), new Function<CommandRef,String>() {
      @Override
      public String apply( CommandRef input ) {
        return input.getName();
      }
    } );
    return toArray( transform, String.class );
  }

  @Override
  public ImageDescriptor getImageDescriptor() {
    return new IconRegistry().getDescriptor( IconRegistry.GIT_PROPOSAL );
  }
}