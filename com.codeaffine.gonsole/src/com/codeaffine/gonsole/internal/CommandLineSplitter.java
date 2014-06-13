package com.codeaffine.gonsole.internal;

import static com.google.common.collect.Iterables.toArray;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

class CommandLineSplitter {

  private final String line;

  CommandLineSplitter( String line ) {
    this.line = line;
  }

  String[] split() {
    Iterable<String> elements = Splitter.onPattern( "\\s+" ).trimResults().split( line );
    Iterable<String> nonEmptyElements = Iterables.filter( elements, new NonEmptyStringFilter() );
    return toArray( nonEmptyElements, String.class );
  }

  private static class NonEmptyStringFilter implements Predicate<String> {
    @Override
    public boolean apply( String input ) {
      return !input.isEmpty();
    }
  }
}