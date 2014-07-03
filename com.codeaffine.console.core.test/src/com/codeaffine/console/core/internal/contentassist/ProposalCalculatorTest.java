package com.codeaffine.console.core.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.test.TestImageDescriptor;
import com.codeaffine.test.util.swt.DisplayHelper;

public class ProposalCalculatorTest {

  private static final String INFO = "info";

  private static final Proposal[] NUMBERS = new Proposal[] {
    newProposal( "one" ),
    newProposal( "two" ),
    newProposal( "three" )
  };
  private static final Proposal[] COLORS = new Proposal[] {
    newProposal( "red" ),
    newProposal( "green" ),
    newProposal( "blue" )
  };

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ProposalCalculator calculator;

  @Test
  public void testCalculate() {
    ICompletionProposal[] actual = calculator.calculate( "", 0, 0 );

    assertThat( actual )
      .hasSize( NUMBERS.length + COLORS.length )
      .isSortedAccordingTo( new ProposalComparator() );
  }

  @Test
  public void testCalculateWithUnkownPrefix() {
    ICompletionProposal[] actual = calculator.calculate( "xxx", 0, 0 );

    assertThat( actual ).isEmpty();
  }

  @Test
  public void testDispose() {
    ICompletionProposal[] proposals = calculator.calculate( "", 0, 0 );

    calculator.dispose();

    assertThat( proposals[ 0 ].getImage().isDisposed() ).isTrue();
  }

  @Test
  public void testProposalContent() {
    ICompletionProposal[] proposals = calculator.calculate( "", 0, 0 );

    assertThat( proposals[ 0 ].getDisplayString() ).isEqualTo( "blue" );
    assertThat( proposals[ 0 ].getAdditionalProposalInfo() ).isEqualTo( "blue" + INFO );
  }

  @Before
  public void setUp() {
    displayHelper.ensureDisplay();
    ContentProposalProvider numbersProvider = stubProposalProvider( NUMBERS );
    ContentProposalProvider colorsProvider = stubProposalProvider( COLORS );
    calculator = new ProposalCalculator( numbersProvider, colorsProvider );
  }

  private static ContentProposalProvider stubProposalProvider( Proposal[] proposals ) {
    ContentProposalProvider result = mock( ContentProposalProvider.class );
    when( result.getContentProposals() ).thenReturn( proposals );
    when( result.getImageDescriptor() ).thenReturn( new TestImageDescriptor() );
    return result;
  }

  private static Proposal newProposal( String text ) {
    return new Proposal( text, text + INFO );
  }
}