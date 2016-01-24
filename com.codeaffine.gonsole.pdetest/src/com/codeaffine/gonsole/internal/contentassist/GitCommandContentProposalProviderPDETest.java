package com.codeaffine.gonsole.internal.contentassist;

import static com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper.createWithSingleChildProvider;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.Proposal;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

public class GitCommandContentProposalProviderPDETest {

  private GitCommandContentProposalProvider proposalProvider;

  @Test
  public void testGetContentProposals() {
    Proposal[] actual = proposalProvider.getContentProposals();

    for( Proposal proposal : actual ) {
      assertThat( proposal.getSortKey() ).isNotNull();
      assertThat( proposal.getText() ).isNotEmpty();
      assertThat( proposal.getInfo() ).isNotEmpty();
      assertThat( proposal.getImageDescriptor() ).isNotNull();
    }
  }

  @Test
  public void testGetContentProposalsIsNotEmpty() {
    Proposal[] actual = proposalProvider.getContentProposals();

    assertThat( actual ).isNotEmpty();
  }

  @Test
  public void testGetContentProposalContainsRemoteCommand() {
    Proposal[] proposals = proposalProvider.getContentProposals();

    assertThat( proposals ).extracting( "text" ).contains( "remote" );
  }

  @Test
  public void testGetContentProposalContainsBlameCommand() {
    Proposal[] proposals = proposalProvider.getContentProposals();

    assertThat( proposals ).extracting( "text" ).contains( "blame" );
  }

  @Test
  public void testGetContentProposalContainsMergeBaseCommand() {
    Proposal[] proposals = proposalProvider.getContentProposals();

    assertThat( proposals ).extracting( "text" ).contains( "merge-base" );
  }

  @Before
  public void setUp() {
    CompositeRepositoryProvider repositoryProvider = createWithSingleChildProvider();
    proposalProvider = new GitCommandContentProposalProvider( repositoryProvider );
  }
}