package com.codeaffine.console.core.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.test.TestImageDescriptor;
import com.codeaffine.test.util.swt.DisplayHelper;

public class ProposalCalculatorTest {

  private static final String[] NUMBERS = new String[] { "one", "two", "three" };
  private static final String[] COLORS = new String[] { "red", "green", "blue" };

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

  @Before
  public void setUp() {
    displayHelper.ensureDisplay();
    ContentProposalProvider numbersProvider = stubProposalProvider( NUMBERS );
    ContentProposalProvider colorsProvider = stubProposalProvider( COLORS );
    calculator = new ProposalCalculator( numbersProvider, colorsProvider );
  }

  private static ContentProposalProvider stubProposalProvider( String[] proposals ) {
    ContentProposalProvider result = mock( ContentProposalProvider.class );
    when( result.getContentProposals() ).thenReturn( proposals );
    when( result.getImageDescriptor() ).thenReturn( new TestImageDescriptor() );
    return result;
  }
}