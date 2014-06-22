package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.ui.console.TextConsoleViewer;

import com.codeaffine.console.core.ConsoleComponentFactory;

public class ContentAssist implements ConsoleContentAssist {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final ContentAssistant contentAssistant;
  private final TextConsoleViewer textViewer;

  public ContentAssist( TextConsoleViewer textViewer, ConsoleComponentFactory consoleComponentFactory  ) {
    this.consoleComponentFactory = consoleComponentFactory;
    this.contentAssistant = new ContentAssistant();
    this.textViewer = textViewer;
  }

  public void install() {
    contentAssistant.enablePrefixCompletion( true );
    contentAssistant.setRepeatedInvocationMode( true );

    final ContentAssistProcessor contentAssistProcessor = new ContentAssistProcessor( consoleComponentFactory );
    contentAssistant.setContentAssistProcessor( contentAssistProcessor, PartitionType.INPUT );
    contentAssistant.install( textViewer );
    textViewer.getTextWidget().addDisposeListener( new DisposeListener() {
      @Override
      public void widgetDisposed( DisposeEvent event ) {
        contentAssistant.uninstall();
        contentAssistProcessor.dispose();
      }
    } );

    textViewer.getTextWidget().addFocusListener( new Editor( textViewer, this ) );
  }

  @Override
  public void showPossibleCompletions() {
    contentAssistant.showPossibleCompletions();
  }
}