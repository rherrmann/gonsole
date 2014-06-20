package com.codeaffine.gonsole.acceptance;

import com.codeaffine.gonsole.internal.Constants;

class GitConsolePrompts {

  static String line( String promptPrefix, String... commands ) {
    String result = promptPrefix + Constants.PROMPT_POSTFIX;
    for( String command : commands ) {
      result += command;
    }
    return result;
  }
}