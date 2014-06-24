package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.gonsole.internal.GitConsoleConstants.PROMPT_POSTFIX;

class GitConsolePrompts {

  static String line( String promptPrefix, String... commands ) {
    String result = promptPrefix + PROMPT_POSTFIX;
    for( String command : commands ) {
      result += command;
    }
    return result;
  }
}