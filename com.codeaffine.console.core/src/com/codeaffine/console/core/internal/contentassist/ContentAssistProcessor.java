package com.codeaffine.console.core.internal.contentassist;

import static com.google.common.collect.Iterables.toArray;

import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Point;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ContentProposalProvider;

public class ContentAssistProcessor implements IContentAssistProcessor {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final ProposalPrefixComputer prefixComputer;
  private final ProposalCalculator proposalCalculator;

  public ContentAssistProcessor( ConsoleComponentFactory consoleComponentFactory ) {
    this.consoleComponentFactory = consoleComponentFactory;
    this.prefixComputer = new ProposalPrefixComputer();
    this.proposalCalculator = new ProposalCalculator();
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

  @Override
  public ICompletionProposal[] computeCompletionProposals( ITextViewer viewer, int offset ) {
    Point range = viewer.getSelectedRange();
    String prefix = prefixComputer.compute( viewer, offset );
    ContentProposalProvider[] proposalProviders = consoleComponentFactory.createProposalProviders();
    List<ICompletionProposal> proposals = proposalCalculator.calculate( proposalProviders, prefix, range.x, range.y );
    return toArray( proposals, ICompletionProposal.class );
  }

  public void dispose() {
    proposalCalculator.dispose();
  }
}