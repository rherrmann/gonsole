package com.codeaffine.console.core.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.Proposal;

public class ProposalComparatorTest {

  private ProposalComparator comparator;

  @Test
  public void testCompareAscending() {
    Proposal proposal1 = createProposal( "a" );
    Proposal proposal2 = createProposal( "b" );

    int actual = comparator.compare( proposal1, proposal2 );

    assertThat( actual ).isLessThan( 0 );
  }

  @Test
  public void testCompareDescending() {
    Proposal proposal1 = createProposal( "b" );
    Proposal proposal2 = createProposal( "a" );

    int actual = comparator.compare( proposal1, proposal2 );

    assertThat( actual ).isGreaterThan( 0 );
  }

  @Test
  public void testCompareEquals() {
    Proposal proposal1 = createProposal( "a" );
    Proposal proposal2 = createProposal( "a" );

    int actual = comparator.compare( proposal1, proposal2 );

    assertThat( actual ).isEqualTo( 0 );
  }

  @Before
  public void setUp() {
    comparator = new ProposalComparator();
  }

  private static Proposal createProposal( String sortKey ) {
    return new Proposal( sortKey, null, null, null );
  }
}