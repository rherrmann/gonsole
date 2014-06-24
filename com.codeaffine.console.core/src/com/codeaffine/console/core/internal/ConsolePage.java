package com.codeaffine.console.core.internal;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.console.TextConsoleViewer;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.internal.contentassist.ContentAssist;

class ConsolePage implements IPageBookViewPage {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final TextConsolePage consolePage;

  ConsolePage( TextConsolePage consolePage, ConsoleComponentFactory factory ) {
    this.consolePage = consolePage;
    this.consoleComponentFactory = factory;
  }

  @Override
  public void init( IPageSite site ) throws PartInitException {
    consolePage.init( site );
  }

  @Override
  public void createControl( Composite parent ) {
    consolePage.createControl( parent );
    installDocumentListener();
    new ContentAssist( consolePage.getViewer(), consoleComponentFactory ).install();
    updateCaretOffset( consolePage.getViewer() );
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
    consolePage.dispose();
  }

  private void installDocumentListener() {
    final TextConsoleViewer viewer = consolePage.getViewer();
    final IDocumentListener documentListener = new IDocumentListener() {
      @Override
      public void documentChanged( DocumentEvent event ) {
        updateCaretOffset( viewer );
      }

      @Override
      public void documentAboutToBeChanged( DocumentEvent event ) {
      }
    };
    final IDocument document = viewer.getDocument();
    document.addDocumentListener( documentListener );
    viewer.getTextWidget().addDisposeListener( new DisposeListener() {
      @Override
      public void widgetDisposed( DisposeEvent event ) {
        document.removeDocumentListener( documentListener );
      }
    } );
  }

  private void updateCaretOffset( final TextConsoleViewer viewer ) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        StyledText textWidget = viewer.getTextWidget();
        if( textWidget != null && !textWidget.isDisposed() ) {
          int documentLength = consolePage.getViewer().getDocument().getLength();
          TextSelection selection = new TextSelection( documentLength, 0 );
          consolePage.getViewer().setSelection( selection, true );
        }
      }
    };
    viewer.getTextWidget().getDisplay().asyncExec( runnable );
  }
}