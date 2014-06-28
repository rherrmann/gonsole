package com.codeaffine.console.core.internal;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

class CommandLineTokenizer {

  private final String commandLine;
  private final List<String> tokens;
  private final StringBuilder currentToken;

  CommandLineTokenizer( String commandLine ) {
    this.commandLine = commandLine;
    this.tokens = newArrayList();
    this.currentToken = new StringBuilder();
  }

  String[] tokenize() {
    tokens.clear();
    boolean insideQuotes = false;
    for( int i = 0; i < commandLine.length(); i++ ) {
      char character = getChar( i );
      if( character == '"' ) {
        appendCurrentToken();
        insideQuotes = !insideQuotes;
      } else if( character == '\\' ) {
        if( getChar( i + 1 ) == '"' || getChar( i + 1 ) == '\\' ) {
          currentToken.append( commandLine.charAt( i + 1 ) );
          i++;
        } else {
          currentToken.append( "\\" );
        }
      } else {
        if( insideQuotes ) {
          currentToken.append( character );
        } else {
          if( Character.isWhitespace( character ) ) {
            appendCurrentToken();
          } else {
            currentToken.append( character );
          }
        }
      }
    }
    appendCurrentToken();
    removeGitPrefixToken();
    return toArray( tokens, String.class );
  }

  private void appendCurrentToken() {
    if( currentToken.length() > 0 ) {
      tokens.add( currentToken.toString() );
      currentToken.setLength( 0 );
    }
  }

  private char getChar( int index ) {
    char result = 0x0000;
    if( index < commandLine.length() ) {
      result = commandLine.charAt( index );
    }
    return result;
  }

  private void removeGitPrefixToken() {
    if( tokens.size() >= 2 && tokens.get( 0 ).equals( "git" ) ) {
      tokens.remove( 0 );
    }
  }
}