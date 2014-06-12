package com.codeaffine.gonsole.pdetest;

import static org.assertj.core.api.Assertions.fail;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.TextConsoleViewer;

class TextViewerChangeObserver {

  private final TextConsoleViewer viewer;

  TextViewerChangeObserver( TextConsoleViewer viewer ) {
    this.viewer = viewer;
  }

  void waitForChange() {
    DocumentChangeObserver observer = new DocumentChangeObserver();
    IDocument document = viewer.getDocument();
    document.addDocumentListener( observer );
    try {
      waitForChange( observer );
    } finally {
      document.removeDocumentListener( observer );
    }
  }

  private void waitForChange( DocumentChangeObserver observer ) {
    long startTime = System.currentTimeMillis();
    while( mustWait( observer ) ) {
      checkTimeout( startTime );
      sleep();
    }
  }

  private void sleep() {
    Display display = viewer.getControl().getDisplay();
    if( !display.readAndDispatch() ) {
      display.sleep();
    }
  }

  private boolean mustWait( DocumentChangeObserver observer ) {
    return !observer.isChanged() && !viewer.getControl().getShell().isDisposed();
  }

  private static void checkTimeout( long startTime ) {
    if( System.currentTimeMillis() - startTime > 10000 ) {
      fail( "Timeout while waiting for the result of command line processing." );
    }
  }
}