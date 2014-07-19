package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.ConsoleInformationControl.createInformationControlCreator;
import static com.codeaffine.console.core.internal.contentassist.ConsoleInformationControlCreator.Appearance.FIXED;
import static com.codeaffine.console.core.internal.contentassist.PartitionType.INPUT;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.internal.ConsoleEditor;

public class ContentAssist implements ConsoleContentAssist, DisposeListener {

  private final ConsoleEditor consoleEditor;
  private final ContentAssistant contentAssistant;
  private final ConsoleComponentFactory consoleComponentFactory;
  private final ContentAssistProcessor contentAssistProcessor;

  public ContentAssist( ConsoleEditor consoleEditor, ConsoleComponentFactory factory ) {
    this( consoleEditor, new ContentAssistant(), factory );
  }


  ContentAssist( ConsoleEditor consoleEditor,
                 ContentAssistant contentAssistant,
                 ConsoleComponentFactory factory )
  {
    this.consoleEditor = consoleEditor;
    this.contentAssistant = contentAssistant;
    this.consoleComponentFactory = factory;
    this.contentAssistProcessor = new ContentAssistProcessor( factory, consoleEditor );
  }

  public void install() {
    contentAssistant.enablePrefixCompletion( true );
    contentAssistant.setRepeatedInvocationMode( true );
    contentAssistant.setContentAssistProcessor( contentAssistProcessor, PartitionType.INPUT );
    contentAssistant.setInformationControlCreator( createInformationControlCreator( FIXED ) );
    contentAssistant.install( consoleEditor.getTextViewer() );
    registerContentAssistAction();
    consoleEditor.getTextViewer().getTextWidget().addDisposeListener( this );
  }

  @Override
  public void showPossibleCompletions() {
    ensurePartitioningIsUpToDate();
    if( consoleEditor.isCaretInLastInputPartition() ) {
      consoleEditor.disableCaretPositionUpdater();
      try {
        contentAssistant.showPossibleCompletions();
      } finally {
        consoleEditor.enabledCaretPositionUpdater();
      }
    } else {
      consoleEditor.getTextViewer().getTextWidget().getDisplay().beep();
    }
  }

  @Override
  public void widgetDisposed( DisposeEvent event ) {
    contentAssistant.uninstall();
    contentAssistProcessor.dispose();
  }

  private void registerContentAssistAction() {
    for( String keySequence : collectActivationKeySequences() ) {
      consoleEditor.addAction( keySequence, new ContentAssistAction( this ) );
    }
  }

  private String[] collectActivationKeySequences() {
    Set<String> activationKeySequences = newHashSet();
    ContentProposalProvider[] proposalProviders = consoleComponentFactory.createProposalProviders();
    for( ContentProposalProvider proposalProvider : proposalProviders ) {
      activationKeySequences.add( proposalProvider.getActivationKeySequence() );
    }
    return toArray( activationKeySequences, String.class );
  }

  private void ensurePartitioningIsUpToDate() {
    if( mustUpdatePartitioning() ) {
      updatePartitioning();
    }
  }

  private boolean mustUpdatePartitioning() {
    return    consoleEditor.getCaretOffset() == consoleEditor.getDocumentLength()
           && !INPUT.equals( consoleEditor.getPartitionType() )
           && consoleEditor.isDocumentChangeAllowed();
  }

  private void updatePartitioning() {
    consoleEditor.fireDocumentChange();
  }
}