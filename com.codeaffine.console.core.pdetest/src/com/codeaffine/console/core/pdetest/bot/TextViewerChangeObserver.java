package com.codeaffine.console.core.pdetest.bot;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.google.common.collect.Iterables.tryFind;
import static org.assertj.core.api.Assertions.fail;

import java.util.Arrays;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.console.IConsoleDocumentPartitioner;
import org.eclipse.ui.console.TextConsoleViewer;

import com.google.common.base.Predicate;

class TextViewerChangeObserver {

  private final TextConsoleViewer viewer;

  TextViewerChangeObserver( TextConsoleViewer viewer ) {
    this.viewer = viewer;
  }

  void waitForChange( String capturedText ) {
    long startTime = System.currentTimeMillis();
    while( !viewer.getControl().getShell().isDisposed()
           && ( capturedText.equals( viewer.getDocument().get() )
                || viewer.getDocument().getLength() == 0 ) )
    {
      checkTimeout( startTime );
      flushEventQueue();
    }
  }

  void waitForColoring( int offset, RGB rgb ) {
    long startTime = System.currentTimeMillis();
    while( !viewer.getControl().getShell().isDisposed()
           && !textColorMatches( offset, rgb ) )
    {
      checkTimeout( startTime );
      flushEventQueue();
    }
  }

  private boolean textColorMatches( int offset, RGB rgb ) {
    StyleRange styleRange = getStyleRangeAt( offset );
    return styleRange != null && rgb.equals( styleRange.foreground.getRGB() );
  }

  private StyleRange[] getStyleRanges() {
    return getDocumentPartitioner().getStyleRanges( 0, viewer.getDocument().getLength() );
  }

  private IConsoleDocumentPartitioner getDocumentPartitioner() {
    return ( IConsoleDocumentPartitioner )viewer.getDocument().getDocumentPartitioner();
  }

  private StyleRange getStyleRangeAt( final int offset ) {
    return tryFind( Arrays.asList( getStyleRanges() ), new Predicate<StyleRange>() {
      @Override
      public boolean apply( StyleRange input ) {
        return input.start <= offset && offset <= input.start + input.length;
      }
    } ).orNull();
  }

  private static void flushEventQueue() {
    flushPendingEvents();
  }

  private static void checkTimeout( long startTime ) {
    if( System.currentTimeMillis() - startTime > 10000 ) {
      fail( "Timeout while waiting for the result of command line processing." );
    }
  }
}