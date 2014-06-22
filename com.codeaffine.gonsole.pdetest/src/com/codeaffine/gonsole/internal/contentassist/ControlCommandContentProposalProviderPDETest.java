package com.codeaffine.gonsole.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.resource.ImageDescriptor;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.internal.activator.IconRegistry;

public class ControlCommandContentProposalProviderPDETest {

  private ControlCommandContentProposalProvider proposalProvider;

  @Test
  public void testGetContentProposals() {
    String[] actuals = proposalProvider.getContentProposals();

    assertThat( actuals ).isEqualTo( ControlCommandContentProposalProvider.COMMANDS );
    assertThat( actuals ).isNotSameAs( ControlCommandContentProposalProvider.COMMANDS );
  }

  @Test
  public void testGetImageDescriptor() {
    ImageDescriptor actual = proposalProvider.getImageDescriptor();

    assertThat( actual ).isSameAs( new IconRegistry().getDescriptor( IconRegistry.CONTROL_PROPOSAL ) );
  }

  @Before
  public void setUp() {
    proposalProvider = new ControlCommandContentProposalProvider();
  }
}