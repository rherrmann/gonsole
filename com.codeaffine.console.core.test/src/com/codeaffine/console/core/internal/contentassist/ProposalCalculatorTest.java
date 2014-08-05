package com.codeaffine.console.core.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Comparator;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.SWT;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.internal.ConsoleEditor;
import com.codeaffine.console.core.test.TestImageDescriptor;
import com.codeaffine.test.util.swt.DisplayHelper;

public class ProposalCalculatorTest {

  private static final KeyStroke ACTIVATION_KEY_SEQUENCE = KeyStroke.getInstance( SWT.TAB );
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

  private ConsoleEditor consoleEditor;
  private ProposalCalculator calculator;

  @Test
  public void testCalculate() {
    ICompletionProposal[] actual = calculator.calculate( "", 0, 0 );

    assertThat( actual )
      .hasSize( NUMBERS.length + COLORS.length )
      .isSortedAccordingTo( new CompletionProposalComparator() );
  }

  @Test
  public void testCalculageWithNonMatchingActivationKeySequence() {
    KeyStroke otherKeyStroke = KeyStroke.getInstance( SWT.ARROW_LEFT );
    when( consoleEditor.getActivationKeyStroke() ).thenReturn( otherKeyStroke );

    ICompletionProposal[] actual = calculator.calculate( "", 0, 0 );

    assertThat( actual ).isEmpty();
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
    ContentProposalProvider numbersProvider = stubProposalProvider( NUMBERS );
    ContentProposalProvider colorsProvider = stubProposalProvider( COLORS );
    consoleEditor = stubEditor();
    calculator = new ProposalCalculator( consoleEditor, numbersProvider, colorsProvider );
  }

  private ConsoleEditor stubEditor() {
    ConsoleEditor result = mock( ConsoleEditor.class );
    when( result.getDisplay() ).thenReturn( displayHelper.getDisplay() );
    when( result.getActivationKeyStroke() ).thenReturn( ACTIVATION_KEY_SEQUENCE );
    return result;
  }

  private static ContentProposalProvider stubProposalProvider( Proposal[] proposals ) {
    ContentProposalProvider result = mock( ContentProposalProvider.class );
    when( result.getContentProposals() ).thenReturn( proposals );
    when( result.getActivationKeySequence() ).thenReturn( ACTIVATION_KEY_SEQUENCE );
    return result;
  }

  private static Proposal newProposal( String text ) {
    return new Proposal( text, text, text + INFO, new TestImageDescriptor() );
  }

  private static class CompletionProposalComparator implements Comparator<ICompletionProposal> {
    @Override
    public int compare( ICompletionProposal proposal1, ICompletionProposal proposal2 ) {
      return proposal1.getDisplayString().compareTo( proposal2.getDisplayString() );
    }
  }
}