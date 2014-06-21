package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.ui.console.TextConsoleViewer;

import com.codeaffine.console.core.ConsoleComponentFactory;

public class ContentAssist {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final TextConsoleViewer textViewer;

  public ContentAssist( TextConsoleViewer textViewer, ConsoleComponentFactory consoleComponentFactory  ) {
    this.textViewer = textViewer;
    this.consoleComponentFactory = consoleComponentFactory;
  }

  public void install() {
    final ContentAssistant contentAssistant = new ContentAssistant();
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

    textViewer.getTextWidget().addFocusListener( new ContentAssistActivation( contentAssistant, textViewer ) );
  }
}