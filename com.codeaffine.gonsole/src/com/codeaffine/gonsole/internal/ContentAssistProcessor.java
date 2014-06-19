package com.codeaffine.gonsole.internal;

import static com.google.common.collect.Iterables.toArray;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ContentProposalProvider;
import com.google.common.collect.Lists;

public class ContentAssistProcessor implements IContentAssistProcessor {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final ResourceManager resourceManager;

  public ContentAssistProcessor( ConsoleComponentFactory consoleComponentFactory ) {
    this.consoleComponentFactory = consoleComponentFactory;
    this.resourceManager = new LocalResourceManager( JFaceResources.getResources() );
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
    for( ContentProposalProvider contentProposalProvider : proposalProviders ) {
      String[] contentProposals = contentProposalProvider.getContentProposals();
      for( int i = 0; i < contentProposals.length; i++ ) {
        String name = contentProposals[ i ];
        String prefix = computePrefix( viewer, offset );
        if( name.startsWith( prefix ) ) {
          ImageDescriptor image = contentProposalProvider.getImageDescriptor();
          ICompletionProposal proposal = addProposal( viewer, image, prefix, name );
          proposals.add( proposal );
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
    resourceManager.dispose();
  }

  private static String computePrefix( ITextViewer textViewer, int documentOffset ) {
    String result = "";
    try {
      ITypedRegion region = textViewer.getDocument().getPartition( documentOffset );
      int partitionOffset = region.getOffset();
      int length = documentOffset - partitionOffset;
      result = textViewer.getDocument().get( partitionOffset, length );
    } catch( BadLocationException ignore ) {
    }
    return result;
  }

  private ICompletionProposal addProposal( ITextViewer textViewer, ImageDescriptor image, String prefix, String proposalString ) {
    Point selectedRange = textViewer.getSelectedRange();
    int offset = selectedRange.x;
    int length = selectedRange.y;
    return createProposal( proposalString, proposalString.substring( prefix.length() ), image, offset, length );
  }

  private ICompletionProposal createProposal( String displayString, String replacementString, ImageDescriptor imageDescriptor, int offset, int length ) {
    int cursorPos = replacementString.length();
    Image image = imageDescriptor == null ? null : resourceManager.createImage( imageDescriptor );
    return new CompletionProposal( replacementString, offset, length, cursorPos, image, displayString, null, null );
  }

}