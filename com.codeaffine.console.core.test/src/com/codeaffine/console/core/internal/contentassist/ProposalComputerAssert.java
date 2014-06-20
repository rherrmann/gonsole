package com.codeaffine.console.core.internal.contentassist;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;

public class ProposalComputerAssert extends AbstractAssert<ProposalComputerAssert, ICompletionProposal> {

  private final ValueCaptor valueCaptor;

  private ProposalComputerAssert( ICompletionProposal actual ) {
    super( actual, ProposalComputerAssert.class );
    valueCaptor = new ValueCaptor( actual );
  }

  public static ProposalComputerAssert assertThat( ICompletionProposal actual ) {
    return new ProposalComputerAssert( actual );
  }

  public ProposalComputerAssert hasReplacement( String expected ) {
    valueCaptor.captureReplacementValues();
    Assertions.assertThat( valueCaptor.getReplacement() ).isEqualTo( expected );
    return this;
  }

  public ProposalComputerAssert hasOffset( int expected ) {
    valueCaptor.captureReplacementValues();
    Assertions.assertThat( valueCaptor.getOffset() ).isEqualTo( expected );
    return this;
  }

  public ProposalComputerAssert hasLength( int expected ) {
    valueCaptor.captureReplacementValues();
    Assertions.assertThat( valueCaptor.getLength() ).isEqualTo( expected );
    return this;
  }

  public ProposalComputerAssert hasImage( Image expected ) {
    Assertions.assertThat( actual.getImage() ).isSameAs( expected );
    return this;
  }

  public ProposalComputerAssert hasDisplayString( String expected ) {
    Assertions.assertThat( actual.getDisplayString() ).isSameAs( expected );
    return this;
  }

  public ProposalComputerAssert hasCursorPosition( int expected ) {
    valueCaptor.captureReplacementValues();
    int cursorPosition = actual.getSelection( null ).x - valueCaptor.getOffset();
    Assertions.assertThat( cursorPosition ).isEqualTo( expected );
    return this;
  }
}