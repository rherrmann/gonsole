package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.ui.console.TextConsoleViewer;

import com.codeaffine.console.core.ConsoleComponentFactory;

public class ContentAssist implements ConsoleContentAssist {

  private final ContentAssistProcessor contentAssistProcessor;
  private final ContentAssistant contentAssistant;
  private final TextConsoleViewer textViewer;
  private final Editor editor;

  public ContentAssist( TextConsoleViewer textViewer, ConsoleComponentFactory consoleComponentFactory  ) {
    this.editor = new Editor( textViewer, this );
    this.contentAssistProcessor = new ContentAssistProcessor( consoleComponentFactory, editor );
    this.contentAssistant = new ContentAssistant();
    this.textViewer = textViewer;
  }

  public void install() {
    contentAssistant.enablePrefixCompletion( true );
    contentAssistant.setRepeatedInvocationMode( true );
    contentAssistant.setContentAssistProcessor( contentAssistProcessor, PartitionType.INPUT );

    contentAssistant.install( textViewer );
    textViewer.getTextWidget().addDisposeListener( new DisposeListener() {
      @Override
      public void widgetDisposed( DisposeEvent event ) {
        contentAssistant.uninstall();
        contentAssistProcessor.dispose();
      }
    } );
    textViewer.getTextWidget().addFocusListener( editor );
  }

  @Override
  public void showPossibleCompletions() {
    contentAssistant.showPossibleCompletions();
  }
}