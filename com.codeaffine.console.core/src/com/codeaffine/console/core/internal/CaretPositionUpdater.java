package com.codeaffine.console.core.internal;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Display;

class CaretPositionUpdater {

  private final TextViewer viewer;
  private final IDocument document;
  private final Display display;
  private final IDocumentListener documentListener;
  private final KeyboardTracker keyboardTracker;
  private boolean enabled;

  CaretPositionUpdater( TextViewer viewer ) {
    this.viewer = viewer;
    this.document = viewer.getDocument();
    this.display = viewer.getTextWidget().getDisplay();
    this.keyboardTracker = new KeyboardTracker();
    this.documentListener = new ConsoleDocumentListener();
    this.enabled = true;
  }

  void install() {
    positionCursorAtEnd();
    document.addDocumentListener( documentListener );
    viewer.getTextWidget().addVerifyKeyListener( keyboardTracker );
    viewer.getTextWidget().addKeyListener( keyboardTracker );
    viewer.getTextWidget().addDisposeListener( new TextWidgetDisposeListener() );
  }

  void enable() {
    enabled = true;
  }

  void disable() {
    enabled = false;
  }

  void positionCursorAtEnd() {
    if( enabled && !keyboardTracker.isKeyPressed() ) {
      display.asyncExec( new PositionCursorRunnable( viewer ) );
    }
  }

  private class ConsoleDocumentListener implements IDocumentListener {
    @Override
    public void documentChanged( DocumentEvent event ) {
      positionCursorAtEnd();
    }

    @Override
    public void documentAboutToBeChanged( DocumentEvent event ) {
    }
  }

  private class TextWidgetDisposeListener implements DisposeListener {
    @Override
    public void widgetDisposed( DisposeEvent event ) {
      document.removeDocumentListener( documentListener );
    }
  }

  private static class KeyboardTracker implements VerifyKeyListener, KeyListener {
    private volatile boolean keyPressed;

    boolean isKeyPressed() {
      return keyPressed;
    }

    @Override
    public void keyPressed( KeyEvent event ) {
      keyPressed = true;
    }

    @Override
    public void keyReleased( KeyEvent event ) {
      keyPressed = false;
    }

    @Override
    public void verifyKey( VerifyEvent event ) {
      keyPressed = true;
    }
  }

  private static class PositionCursorRunnable implements Runnable {
    private final TextViewer viewer;

    PositionCursorRunnable( TextViewer viewer ) {
      this.viewer = viewer;
    }

    @Override
    public void run() {
      if( !isViewerDisposed() ) {
        ISelection selection = new TextSelection( viewer.getDocument().getLength(), 0 );
        viewer.setSelection( selection, true );
      }
    }

    private boolean isViewerDisposed() {
      StyledText textWidget = viewer.getTextWidget();
      return textWidget == null || textWidget.isDisposed();
    }
  }
}