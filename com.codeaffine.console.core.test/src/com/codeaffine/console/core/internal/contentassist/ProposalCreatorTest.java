package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.ProposalComputerAssert.assertThat;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.test.TestImageDescriptor;
import com.codeaffine.test.util.swt.DisplayHelper;

public class ProposalCreatorTest {

  private final static int SELECTION_LENGTH = 4711;

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ProposalCreator creator;
  private Image image;
  private Proposal proposal;

  @Test
  public void testCreateWithEmptyLine() {
    int start = 0;

    ICompletionProposal actual = creator.create( "", proposal, start, SELECTION_LENGTH, image );

    assertThat( actual )
      .hasReplacement( proposal.getText() )
      .hasOffset( start )
      .hasLength( SELECTION_LENGTH )
      .hasCursorPosition( proposal.getText().length() )
      .hasImage( image )
      .hasDisplayString( proposal.getText() )
      .hasAdditionalInfo( proposal.getInfo() );
  }

  @Test
  public void testCreateWithMatchingPrefix() {
    int start = 1;

    ICompletionProposal actual = creator.create( "p", proposal, start, SELECTION_LENGTH, image );

    assertThat( actual )
      .hasReplacement( "roposal" )
      .hasOffset( start )
      .hasLength( SELECTION_LENGTH )
      .hasImage( image )
      .hasCursorPosition( "roposal".length() )
      .hasDisplayString( proposal.getText() )
      .hasAdditionalInfo( proposal.getInfo() );
  }

  @Test
  public void testCreateWithNonMatchingPrefix() {
    ICompletionProposal actual = creator.create( "x", proposal, 1, SELECTION_LENGTH, image );

    assertThat( actual ).isNull();
  }

  @Test
  public void testCreateWithMatchingPrefixAndStartOffset() {
    int start = 0;

    ICompletionProposal actual = creator.create( "", proposal, start, SELECTION_LENGTH, image );

    assertThat( actual )
      .hasReplacement( proposal.getText() )
      .hasOffset( start )
      .hasLength( SELECTION_LENGTH )
      .hasImage( image )
      .hasCursorPosition( proposal.getText().length() )
      .hasDisplayString( proposal.getText() )
      .hasAdditionalInfo( proposal.getInfo() );
  }

  @Before
  public void setUp() {
    image = new TestImageDescriptor().createImage( displayHelper.getDisplay() );
    proposal = new Proposal( "proposal", "additinal-info" );
    creator = new ProposalCreator();
  }
}