package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.ProposalComputerAssert.assertThat;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.test.TestImageDescriptor;
import com.codeaffine.test.util.swt.DisplayHelper;

public class ProposalComputerTest {

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ProposalComputer computer;
  private Image image;

  @Test
  public void testComputeWithEmptyLine() {
    int start = 0;
    int length = 0;

    ICompletionProposal actual = computer.compute( "", start, length, "proposal", image );

    assertThat( actual )
      .hasReplacement( "proposal" )
      .hasOffset( start )
      .hasLength( length )
      .hasCursorPosition( "proposal".length() )
      .hasImage( image )
      .hasDisplayString( "proposal" );
  }

  @Test
  public void testComputeWithPartialCommand() {
    int start = 1;
    int length = 0;

    ICompletionProposal actual = computer.compute( "p", start, length, "proposal", image );

    assertThat( actual )
      .hasReplacement( "roposal" )
      .hasOffset( start )
      .hasLength( length )
      .hasImage( image )
      .hasCursorPosition( "roposal".length() )
      .hasDisplayString( "proposal" );
  }

  @Test
  public void testComputeWithPartialCommandAndEndSelection() {
    int start = 1;
    int length = 1;

    ICompletionProposal actual = computer.compute( "p", start, length, "proposal", image );

    assertThat( actual )
      .hasReplacement( "roposal" )
      .hasOffset( start )
      .hasLength( length )
      .hasImage( image )
      .hasCursorPosition( "roposal".length() )
      .hasDisplayString( "proposal" );
  }

  @Test
  public void testComputeWithPartialCommandAndStartSelection() {
    int start = 0;
    int length = 1;

    ICompletionProposal actual = computer.compute( "", start, length, "proposal", image );

    assertThat( actual )
      .hasReplacement( "proposal" )
      .hasOffset( start )
      .hasLength( length )
      .hasImage( image )
      .hasCursorPosition( "proposal".length() )
      .hasDisplayString( "proposal" );
  }

  @Before
  public void setUp() {
    image = new TestImageDescriptor().createImage( displayHelper.getDisplay() );
    computer = new ProposalComputer();
  }
}