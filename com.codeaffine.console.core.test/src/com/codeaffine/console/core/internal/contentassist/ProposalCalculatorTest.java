package com.codeaffine.console.core.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

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
  private ContentProposalProvider numbersProvider;
  private ContentProposalProvider colorsProvider;

  @Test
  public void testCalculate() {
    List<ICompletionProposal> actual = calculator.calculate( "", 0, 0, numbersProvider, colorsProvider );

    assertThat( actual )
      .hasSize( NUMBERS.length + COLORS.length )
      .isSortedAccordingTo( new ProposalComparator() );
  }

  @Test
  public void testCalculateWithUnkownPrefix() {
    List<ICompletionProposal> actual = calculator.calculate( "xxx", 0, 0, numbersProvider, colorsProvider );

    assertThat( actual ).isEmpty();
  }

  @Test
  public void testDispose() {
    List<ICompletionProposal> proposals = calculator.calculate( "", 0, 0, numbersProvider, colorsProvider );

    calculator.dispose();

    assertThat( proposals.get( 0 ).getImage().isDisposed() ).isTrue();
  }

  @Before
  public void setUp() {
    displayHelper.ensureDisplay();
    calculator = new ProposalCalculator();
    numbersProvider = stubProposalProvider( NUMBERS );
    colorsProvider = stubProposalProvider( COLORS );
  }

  private static ContentProposalProvider stubProposalProvider( String[] proposals ) {
    ContentProposalProvider result = mock( ContentProposalProvider.class );
    when( result.getContentProposals() ).thenReturn( proposals );
    when( result.getImageDescriptor() ).thenReturn( new TestImageDescriptor() );
    return result;
  }
}