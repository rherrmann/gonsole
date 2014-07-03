package com.codeaffine.gonsole.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jgit.pgm.CommandCatalog;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.Proposal;
import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.interpreter.PgmResourceBundle;

public class GitCommandContentProposalProviderPDETest {

  private GitCommandContentProposalProvider proposalProvider;

  @Test
  public void testGetContentProposals() {
    Proposal[] actual = proposalProvider.getContentProposals();

    assertThat( actual ).hasSize( CommandCatalog.common().length );
    assertThat( actual[ 0 ].getText() ).isEqualTo( CommandCatalog.common()[ 0 ].getName() );
    assertThat( actual[ 0 ].getInfo() ).isNotEmpty();
  }

  @Test
  public void testGetImageDescriptor() {
    ImageDescriptor actual = proposalProvider.getImageDescriptor();

    assertThat( actual ).isSameAs( new IconRegistry().getDescriptor( IconRegistry.GIT_PROPOSAL ) );
  }

  @Before
  public void setUp() {
    proposalProvider = new GitCommandContentProposalProvider();
    new PgmResourceBundle().reset();
  }
}