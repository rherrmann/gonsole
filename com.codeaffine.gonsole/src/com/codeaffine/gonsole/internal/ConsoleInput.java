package com.codeaffine.gonsole.internal;

import java.util.NoSuchElementException;
import java.util.Scanner;


public class ConsoleInput {

  private final ConsoleIOProvider consoleIOProvider;

  public ConsoleInput( ConsoleIOProvider consoleIOProvider ) {
    this.consoleIOProvider = consoleIOProvider;
  }

  public String readLine() {
    Scanner scanner = new Scanner( consoleIOProvider.getInputStream() );
    String result;
    try {
      result = scanner.nextLine();
    } catch( NoSuchElementException endOfFile ) {
      result = null;
    }
    return result;
  }
}
