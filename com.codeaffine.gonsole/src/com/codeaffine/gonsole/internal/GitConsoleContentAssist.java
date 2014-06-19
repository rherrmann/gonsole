package com.codeaffine.gonsole.internal;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IDocumentPartitionerExtension;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.TextConsoleViewer;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.console.IOConsolePartition;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

import com.codeaffine.gonsole.ConsoleComponentFactory;

class GitConsoleContentAssist {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final TextConsoleViewer textViewer;

  public GitConsoleContentAssist( TextConsoleViewer textViewer, ConsoleComponentFactory consoleComponentFactory  ) {
    this.textViewer = textViewer;
    this.consoleComponentFactory = consoleComponentFactory;
  }

  void install() {
    final ContentAssistant contentAssistant = new ContentAssistant();
    contentAssistant.enablePrefixCompletion( true );
    contentAssistant.setRepeatedInvocationMode( true );
    final ContentAssistProcessor contentAssistProcessor = new ContentAssistProcessor( consoleComponentFactory );
    contentAssistant.setContentAssistProcessor( contentAssistProcessor, IOConsolePartition.INPUT_PARTITION_TYPE );
    contentAssistant.install( textViewer );
    textViewer.getTextWidget().addDisposeListener( new DisposeListener() {
      @Override
      public void widgetDisposed( DisposeEvent event ) {
        contentAssistant.uninstall();
        contentAssistProcessor.dispose();
      }
    } );

    textViewer.getTextWidget().addFocusListener( new FocusListener() {
      private IHandlerActivation activation;

      @Override
      public void focusGained( FocusEvent event ) {
        Action action = new Action() {
          @Override
          public void run() {
            IDocument document = textViewer.getDocument();
            int caretOffset = textViewer.getTextWidget().getCaretOffset();
            if( caretOffset == textViewer.getDocument().getLength() ) {
              ITypedRegion partition = null;
              try {
                partition = document.getPartition( caretOffset );
              } catch( BadLocationException ignore ) {
              }
              if( partition == null || !IOConsolePartition.INPUT_PARTITION_TYPE.equals( partition.getType() ) ) {
                IDocumentPartitioner partitioner = document.getDocumentPartitioner();
                if( partitioner instanceof IDocumentPartitionerExtension ) {
                  IDocumentPartitionerExtension partitionerExtension = ( IDocumentPartitionerExtension )partitioner;
                  DocumentEvent event = new DocumentEvent( document, caretOffset, 0, "" );
                  partitionerExtension.documentChanged2( event );
                }
              }
              }
            contentAssistant.showPossibleCompletions();
          }
        };
        action.setActionDefinitionId( ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS );
        action.setEnabled( true );
        String actionId = action.getActionDefinitionId();
        ActionHandler handler = new ActionHandler( action );
        IHandlerService handlerService = ( IHandlerService )PlatformUI.getWorkbench().getService( IHandlerService.class );
        activation = handlerService.activateHandler( actionId, handler, null );
      }

      @Override
      public void focusLost( FocusEvent event ) {
        if( activation != null ) {
          IHandlerService handlerService
            = ( IHandlerService )PlatformUI.getWorkbench().getService( IHandlerService.class );
          handlerService.deactivateHandler( activation );
          activation = null;
        }
      }
    } );
  }
}