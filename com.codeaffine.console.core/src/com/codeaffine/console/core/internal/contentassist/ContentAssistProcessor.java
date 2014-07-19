package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Point;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.internal.ConsoleEditor;

public class ContentAssistProcessor implements IContentAssistProcessor {

  private final ProposalCalculator proposalCalculator;
  private final ConsoleEditor consoleEditor;

  public ContentAssistProcessor( ConsoleComponentFactory consoleComponentFactory, ConsoleEditor consoleEditor ) {
    this( new ProposalCalculator( consoleEditor, consoleComponentFactory.createProposalProviders() ), consoleEditor );
  }

  ContentAssistProcessor( ProposalCalculator proposalCalculator, ConsoleEditor consoleEditor ) {
    this.proposalCalculator = proposalCalculator;
    this.consoleEditor = consoleEditor;
  }

  public void dispose() {
    proposalCalculator.dispose();
  }

  @Override
  public ICompletionProposal[] computeCompletionProposals( ITextViewer viewer, int offset ) {
    Point range = viewer.getSelectedRange();
    String prefix = consoleEditor.computePrefix( offset );
    return proposalCalculator.calculate( prefix, range.x, range.y );
  }

  @Override
  public String getErrorMessage() {
    return null;
  }

  @Override
  public IContextInformationValidator getContextInformationValidator() {
    return null;
  }

  @Override
  public char[] getContextInformationAutoActivationCharacters() {
    return null;
  }

  @Override
  public char[] getCompletionProposalAutoActivationCharacters() {
    return null;
  }

  @Override
  public IContextInformation[] computeContextInformation( ITextViewer viewer, int offset ) {
    return null;
  }
}