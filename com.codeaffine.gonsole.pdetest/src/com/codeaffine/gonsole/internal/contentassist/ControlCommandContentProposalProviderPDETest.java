package com.codeaffine.gonsole.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.Proposal;

public class ControlCommandContentProposalProviderPDETest {

  private ControlCommandContentProposalProvider proposalProvider;

  @Test
  public void testGetContentProposals() {
    Proposal[] actuals = proposalProvider.getContentProposals();

    assertThat( actuals ).isSameAs( ControlCommandContentProposalProvider.PROPOSALS );
  }

  @Before
  public void setUp() {
    proposalProvider = new ControlCommandContentProposalProvider();
  }
}