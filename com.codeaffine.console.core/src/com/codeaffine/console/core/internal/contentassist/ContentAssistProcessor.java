package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Point;

import com.codeaffine.console.core.ConsoleComponentFactory;

public class ContentAssistProcessor implements IContentAssistProcessor {

  private final ProposalCalculator proposalCalculator;
  private final Editor editor;

  public ContentAssistProcessor( ConsoleComponentFactory consoleComponentFactory, Editor editor ) {
    this( new ProposalCalculator( editor, consoleComponentFactory.createProposalProviders() ), editor );
  }

  ContentAssistProcessor( ProposalCalculator proposalCalculator, Editor editor ) {
    this.proposalCalculator = proposalCalculator;
    this.editor = editor;
  }

  public void dispose() {
    proposalCalculator.dispose();
  }

  @Override
  public ICompletionProposal[] computeCompletionProposals( ITextViewer viewer, int offset ) {
    Point range = viewer.getSelectedRange();
    String prefix = editor.computePrefix( offset );
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