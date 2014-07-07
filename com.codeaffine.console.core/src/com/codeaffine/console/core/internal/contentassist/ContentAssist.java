package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.ConsoleInformationControl.createInformationControlCreator;
import static com.codeaffine.console.core.internal.contentassist.ConsoleInformationControlCreator.Appearance.FIXED;
import static com.codeaffine.console.core.internal.contentassist.PartitionType.INPUT;

import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import com.codeaffine.console.core.ConsoleComponentFactory;

public class ContentAssist implements ConsoleContentAssist, DisposeListener {

  private final Editor editor;
  private final ContentAssistant contentAssistant;
  private final ContentAssistProcessor contentAssistProcessor;

  public ContentAssist( TextViewer textViewer, ConsoleComponentFactory factory  ) {
    this( new Editor( textViewer ), new ContentAssistant(), factory );
  }

  ContentAssist( Editor editor, ContentAssistant contentAssistant, ConsoleComponentFactory factory )
  {
    this.editor = editor;
    this.contentAssistant = contentAssistant;
    this.contentAssistProcessor = new ContentAssistProcessor( factory, editor );
  }

  public void install() {
    contentAssistant.enablePrefixCompletion( true );
    contentAssistant.setRepeatedInvocationMode( true );
    contentAssistant.setContentAssistProcessor( contentAssistProcessor, PartitionType.INPUT );
    contentAssistant.setInformationControlCreator( createInformationControlCreator( FIXED ) );
    contentAssistant.install( editor.getTextViewer() );
    editor.addAction( "Ctrl+Space", new ContentAssistAction( this ) );
    editor.getTextViewer().getTextWidget().addDisposeListener( this );
  }

  @Override
  public void showPossibleCompletions() {
    ensurePartitioningIsUpToDate();
    if( editor.isCaretInLastInputPartition() ) {
      contentAssistant.showPossibleCompletions();
    } else {
      editor.getTextViewer().getTextWidget().getDisplay().beep();
    }
  }

  @Override
  public void widgetDisposed( DisposeEvent event ) {
    contentAssistant.uninstall();
    contentAssistProcessor.dispose();
  }

  private void ensurePartitioningIsUpToDate() {
    if( mustUpdatePartitioning() ) {
      updatePartitioning();
    }
  }

  private boolean mustUpdatePartitioning() {
    return    editor.getCaretOffset() == editor.getDocumentLength()
           && !INPUT.equals( editor.getPartitionType() )
           && editor.isDocumentChangeAllowed();
  }

  private void updatePartitioning() {
    editor.fireDocumentChange();
  }
}