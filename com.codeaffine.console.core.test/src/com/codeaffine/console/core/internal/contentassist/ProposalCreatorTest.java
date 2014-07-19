package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.ProposalComputerAssert.assertThat;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.internal.ConsoleEditor;
import com.codeaffine.console.core.test.TestImageDescriptor;
import com.codeaffine.test.util.swt.DisplayHelper;

public class ProposalCreatorTest {

  private final static int SELECTION_LENGTH = 4711;
  private static final String SPACE = " ";

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private TextViewer textViewer;
  private ProposalCreator creator;
  private Image image;
  private Proposal proposal;

  @Test
  public void testCreateWithEmptyLine() {
    int start = 0;

    ICompletionProposal actual = creator.create( "", proposal, start, SELECTION_LENGTH );

    assertThat( actual )
      .hasReplacement( proposal.getText() + SPACE )
      .hasOffset( start )
      .hasLength( SELECTION_LENGTH )
      .hasCursorPosition( proposal.getText().length() + SPACE.length() )
      .hasImage( image )
      .hasDisplayString( proposal.getText() )
      .hasAdditionalInfo( proposal.getInfo() );
  }

  @Test
  public void testCreateWithMatchingPrefix() {
    int start = 1;

    ICompletionProposal actual = creator.create( "p", proposal, start, SELECTION_LENGTH );

    assertThat( actual )
      .hasReplacement( "roposal" + SPACE )
      .hasOffset( start )
      .hasLength( SELECTION_LENGTH )
      .hasImage( image )
      .hasCursorPosition( "roposal".length() + SPACE.length() )
      .hasDisplayString( proposal.getText() )
      .hasAdditionalInfo( proposal.getInfo() );
  }

  @Test
  public void testCreateWithNonMatchingPrefix() {
    ICompletionProposal actual = creator.create( "x", proposal, 1, SELECTION_LENGTH );

    assertThat( actual ).isNull();
  }

  @Test
  public void testCreateWithMatchingPrefixAndStartOffset() {
    int start = 0;

    ICompletionProposal actual = creator.create( "", proposal, start, SELECTION_LENGTH );

    assertThat( actual )
      .hasReplacement( proposal.getText() + SPACE )
      .hasOffset( start )
      .hasLength( SELECTION_LENGTH )
      .hasImage( image )
      .hasCursorPosition( proposal.getText().length() + SPACE.length() )
      .hasDisplayString( proposal.getText() )
      .hasAdditionalInfo( proposal.getInfo() );
  }

  @Test
  public void testCreateWithCaretPositionInText() {
    int start = 1;
    textViewer.getDocument().set( "pr" );
    textViewer.getTextWidget().setCaretOffset( 1 );

    ICompletionProposal actual = creator.create( "p", proposal, start, SELECTION_LENGTH );

    assertThat( actual )
      .hasReplacement( "roposal" )
      .hasOffset( start )
      .hasLength( SELECTION_LENGTH )
      .hasImage( image )
      .hasCursorPosition( "roposal".length() )
      .hasDisplayString( proposal.getText() )
      .hasAdditionalInfo( proposal.getInfo() );
  }

  @Before
  public void setUp() {
    image = new TestImageDescriptor().createImage( displayHelper.getDisplay() );
    textViewer = new TextViewer( displayHelper.createShell(), SWT.NONE );
    Document document = new Document();
    textViewer.setDocument( document );
    proposal = new Proposal( "proposal", "proposal", "additinal-info", new TestImageDescriptor() );
    creator = new ProposalCreator( new ConsoleEditor( textViewer ) );
  }

  @After
  public void tearDown() {
    image.dispose();
    creator.dispose();
  }
}