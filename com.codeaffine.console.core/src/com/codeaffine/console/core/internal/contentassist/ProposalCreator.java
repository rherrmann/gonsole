package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;

import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.internal.ConsoleEditor;
import com.codeaffine.console.core.internal.resource.ResourceRegistry;

class ProposalCreator {

  private final ConsoleEditor consoleEditor;
  private final ResourceRegistry imageRegistry;

  ProposalCreator( ConsoleEditor consoleEditor ) {
    this.consoleEditor = consoleEditor;
    this.imageRegistry = new ResourceRegistry( consoleEditor.getDisplay() );
  }

  ICompletionProposal create( String prefix, Proposal proposal, int start, int length ) {
    ICompletionProposal result = null;
    if( proposal.getText().startsWith( prefix ) ) {
      result = doCreate( prefix, proposal, start, length );
    }
    return result;
  }

  void dispose() {
    imageRegistry.dispose();
  }

  private ICompletionProposal doCreate( String prefix, Proposal proposal, int start, int length ) {
    String text = proposal.getText();
    String replacement = getReplacement( prefix, text );
    int cursorPosition = replacement.length();
    Image image = imageRegistry.getImage( proposal.getImageDescriptor() );
    String info = proposal.getInfo();
    return new CompletionProposal( replacement, start, length, cursorPosition, image, text, null, info );
  }

  private String getReplacement( String prefix, String text ) {
    String result = text.substring( prefix.length() );
    if( consoleEditor.getCaretOffset() == consoleEditor.getDocumentLength() ) {
      result += " ";
    }
    return result;
  }
}