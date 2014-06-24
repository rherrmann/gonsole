package com.codeaffine.console.core.internal;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.ui.console.TextConsoleViewer;

class CaretPositionUpdater {

  private final TextConsoleViewer viewer;
  private final IDocument document;
  private final IDocumentListener documentListener;

  CaretPositionUpdater( TextConsoleViewer viewer ) {
    this.viewer = viewer;
    this.document = viewer.getDocument();
    this.documentListener = new IDocumentListener() {
      @Override
      public void documentChanged( DocumentEvent event ) {
        positionCursorAtEnd();
      }

      @Override
      public void documentAboutToBeChanged( DocumentEvent event ) {
      }
    };
  }

  void install() {
    positionCursorAtEnd();
    document.addDocumentListener( documentListener );
    viewer.getTextWidget().addDisposeListener( new DisposeListener() {
      @Override
      public void widgetDisposed( DisposeEvent event ) {
        document.removeDocumentListener( documentListener );
      }
    } );
  }

  void positionCursorAtEnd() {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        StyledText textWidget = viewer.getTextWidget();
        if( textWidget != null && !textWidget.isDisposed() ) {
          int documentLength = viewer.getDocument().getLength();
          TextSelection selection = new TextSelection( documentLength, 0 );
          viewer.setSelection( selection, true );
        }
      }
    };
    viewer.getTextWidget().getDisplay().asyncExec( runnable );
  }
}