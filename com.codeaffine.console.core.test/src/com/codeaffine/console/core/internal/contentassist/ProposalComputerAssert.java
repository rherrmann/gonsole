package com.codeaffine.console.core.internal.contentassist;

import org.assertj.core.api.AbstractAssert;
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
    if( !expected.equals( valueCaptor.getReplacement() ) ) {
      failWithMessage( "Expected replacement to be <%s> but was <%s>", expected, valueCaptor.getReplacement() );
    }
    return this;
  }

  public ProposalComputerAssert hasOffset( int expected ) {
    valueCaptor.captureReplacementValues();
    if( expected != valueCaptor.getOffset() ) {
      failWithMessage( "Expected offset to be <%s> but was <%s>", expected, valueCaptor.getOffset() );
    }
    return this;
  }

  public ProposalComputerAssert hasLength( int expected ) {
    valueCaptor.captureReplacementValues();
    if( expected != valueCaptor.getLength() ) {
      failWithMessage( "Expected length to be <%s> but was <%s>", expected, valueCaptor.getLength() );
    }
    return this;
  }

  public ProposalComputerAssert hasImage( Image expected ) {
    if( actual.getImage() != expected ) {
      failWithMessage( "Expected image be <%s> but was <%s>", expected, actual.getImage() );
    }
    return this;
  }

  public ProposalComputerAssert hasDisplayString( String expected ) {
    if( !expected.equals( actual.getDisplayString() ) ) {
      failWithMessage( "Expected displayString to be <%s> but was <%s>", expected, actual.getDisplayString() );
    }
    return this;
  }

  public ProposalComputerAssert hasCursorPosition( int expected ) {
    valueCaptor.captureReplacementValues();
    int cursorPosition = actual.getSelection( null ).x - valueCaptor.getOffset();
    if( expected != cursorPosition ) {
      failWithMessage( "Expected cursorPosition to be <%s> but was <%s>", expected, cursorPosition );
    }
    return this;
  }

  public ProposalComputerAssert hasAdditionalInfo( String expected ) {
    String actualnfo = actual.getAdditionalProposalInfo();
    if( !expected.equals( actualnfo ) ) {
      failWithMessage( "Expected additionalInfo to be <%s> but was <%s>", expected, actualnfo );
    }
    return this;
  }
}