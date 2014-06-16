package com.codeaffine.gonsole.internal;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Iterables.skip;
import static com.google.common.collect.Iterables.toArray;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;

class CommandLineSplitter {

  private final String line;

  CommandLineSplitter( String line ) {
    this.line = line;
  }

  String[] split() {
    Iterable<String> elements = Splitter.onPattern( "\\s+" ).trimResults().split( line );
    Iterable<String> nonEmptyElements = filter( elements, new NonEmptyStringFilter() );
    if( size( nonEmptyElements ) >= 2 && getFirst( nonEmptyElements, "" ).equals( "git" ) ) {
      nonEmptyElements = skip( nonEmptyElements, 1 );
    }
    return toArray( nonEmptyElements, String.class );
  }

  private static class NonEmptyStringFilter implements Predicate<String> {
    @Override
    public boolean apply( String input ) {
      return !input.isEmpty();
    }
  }
}