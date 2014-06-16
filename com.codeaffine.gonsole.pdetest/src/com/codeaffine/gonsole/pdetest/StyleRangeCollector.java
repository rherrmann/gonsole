package com.codeaffine.gonsole.pdetest;

import static com.google.common.collect.Iterables.tryFind;
import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

import com.google.common.base.Predicate;

class StyleRangeCollector implements LineStyleListener {
  private final Map<Integer,StyleRange> styleRanges;

  StyleRangeCollector( StyledText styledText ) {
    styledText.addLineStyleListener( this );
    styleRanges = newHashMap();
  }

  @Override
  public void lineGetStyle( LineStyleEvent event ) {
    StyleRange[] styles = event.styles;
    if( styles != null ) {
      for( StyleRange style : styles ) {
        styleRanges.put( style.start, style );
      }
    }
  }

  StyleRange getStyleRangeAt( final int offset ) {
    return tryFind( styleRanges.values(), new Predicate<StyleRange>() {
      @Override
      public boolean apply( StyleRange input ) {
        return input.start <= offset && offset <= input.start + input.length;
      }
    } ).orNull();
  }
}