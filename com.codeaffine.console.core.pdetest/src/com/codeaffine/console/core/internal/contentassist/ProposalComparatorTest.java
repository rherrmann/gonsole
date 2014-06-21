package com.codeaffine.console.core.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.junit.Before;
import org.junit.Test;

public class ProposalComparatorTest {

  private ProposalComparator comparator;

  @Test
  public void testCompareAscending() {
    ICompletionProposal proposal1 = stubProposalWith( "a" );
    ICompletionProposal proposal2 = stubProposalWith( "b" );

    int actual = comparator.compare( proposal1, proposal2 );

    assertThat( actual ).isLessThan( 0 );
  }

  @Test
  public void testCompareDescending() {
    ICompletionProposal proposal1 = stubProposalWith( "b" );
    ICompletionProposal proposal2 = stubProposalWith( "a" );

    int actual = comparator.compare( proposal1, proposal2 );

    assertThat( actual ).isGreaterThan( 0 );
  }

  @Test
  public void testCompareEquals() {
    ICompletionProposal proposal1 = stubProposalWith( "a" );
    ICompletionProposal proposal2 = stubProposalWith( "a" );

    int actual = comparator.compare( proposal1, proposal2 );

    assertThat( actual ).isEqualTo( 0 );
  }

  @Before
  public void setUp() {
    comparator = new ProposalComparator();
  }

  private static ICompletionProposal stubProposalWith( String displayString ) {
    ICompletionProposal result = mock( ICompletionProposal.class );
    when( result.getDisplayString() ).thenReturn( displayString );
    return result;
  }
}