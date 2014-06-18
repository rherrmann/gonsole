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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.console.TextConsoleViewer;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.console.IOConsolePartition;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

class GitConsolePage implements IPageBookViewPage {

  private final TextConsolePage consolePage;
  private final InputObserver inputObserver;

  GitConsolePage( TextConsolePage consolePage,
                  ConsoleIOProvider consoleIOProvider,
                  CompositeRepositoryProvider repositoryProvider )
  {
    this.consolePage = consolePage;
    this.inputObserver = new InputObserver( consoleIOProvider, repositoryProvider );
  }

  @Override
  public void init( IPageSite site ) throws PartInitException {
    consolePage.init( site );
  }

  @Override
  public void createControl( Composite parent ) {
    consolePage.createControl( parent );

    TextConsoleViewer sourceViewer = consolePage.getViewer();

    final ContentAssistant contentAssistant = new ContentAssistant();
    contentAssistant.enablePrefixCompletion( true );
    contentAssistant.setRepeatedInvocationMode( true );
    final ContentAssistProcessor contentAssistProcessor = new ContentAssistProcessor();
    contentAssistant.setContentAssistProcessor( contentAssistProcessor, IOConsolePartition.INPUT_PARTITION_TYPE );
    contentAssistant.install( sourceViewer );
    sourceViewer.getTextWidget().addDisposeListener( new DisposeListener() {
      @Override
      public void widgetDisposed( DisposeEvent event ) {
        contentAssistant.uninstall();
        contentAssistProcessor.dispose();
      }
    } );

    sourceViewer.getTextWidget().addFocusListener( new FocusListener() {
      private IHandlerActivation activation;

      @Override
      public void focusGained( FocusEvent event ) {
        Action action = new Action() {
          @Override
          public void run() {
            TextConsoleViewer viewer = consolePage.getViewer();
            IDocument document = viewer.getDocument();

            int caretOffset = viewer.getTextWidget().getCaretOffset();
            if( caretOffset == viewer.getDocument().getLength() ) {
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
//            viewer.doOperation( ISourceViewer.CONTENTASSIST_PROPOSALS );
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


    inputObserver.start( consolePage.getViewer() );
  }

  @Override
  public void setFocus() {
    consolePage.setFocus();
  }

  @Override
  public void setActionBars( IActionBars actionBars ) {
    consolePage.setActionBars( actionBars );
  }

  @Override
  public Control getControl() {
    return consolePage.getControl();
  }

  @Override
  public IPageSite getSite() {
    return consolePage.getSite();
  }

  @Override
  public void dispose() {
    inputObserver.stop();
    consolePage.dispose();
  }
}