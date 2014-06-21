package com.codeaffine.console.core.internal.contentassist;

import static com.google.common.collect.Iterables.toArray;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.internal.resource.ResourceRegistry;
import com.google.common.collect.Lists;

public class ContentAssistProcessor implements IContentAssistProcessor {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final ResourceRegistry imageRegistry;
  private final ProposalComputer proposalComputer;
  private final PrefixComputer prefixComputer;

  public ContentAssistProcessor( ConsoleComponentFactory consoleComponentFactory ) {
    this.imageRegistry = new ResourceRegistry();
    this.consoleComponentFactory = consoleComponentFactory;
    this.proposalComputer = new ProposalComputer();
    this.prefixComputer = new PrefixComputer();
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
    return new char[] { '.' };
  }

  @Override
  public IContextInformation[] computeContextInformation( ITextViewer viewer, int offset ) {
    return null;
  }

  @Override
  public ICompletionProposal[] computeCompletionProposals( ITextViewer viewer, int offset ) {
    ContentProposalProvider[] proposalProviders = consoleComponentFactory.createProposalProviders();
    List<ICompletionProposal> proposals = Lists.newArrayList();
    for( ContentProposalProvider proposalProvider : proposalProviders ) {
      String[] contentProposals = proposalProvider.getContentProposals();
      for( int i = 0; i < contentProposals.length; i++ ) {
        String name = contentProposals[ i ];
        String prefix = prefixComputer.compute( viewer, offset );
        if( name.startsWith( prefix ) ) {
          Image image = imageRegistry.getImage( proposalProvider.getImageDescriptor() );
          Point selectedRange = viewer.getSelectedRange();
          proposals.add( proposalComputer.compute( prefix, selectedRange.x, selectedRange.y, name, image ) );
        }
      }
    }
    Collections.sort( proposals, new Comparator<ICompletionProposal>() {
      @Override
      public int compare( ICompletionProposal proposal1, ICompletionProposal proposal2 ) {
        return proposal1.getDisplayString().compareTo( proposal2.getDisplayString() );
      }
    } );
    return toArray( proposals, ICompletionProposal.class );
  }

  public void dispose() {
    imageRegistry.dispose();
  }
}