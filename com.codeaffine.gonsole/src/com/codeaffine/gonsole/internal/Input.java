package com.codeaffine.gonsole.internal;

import java.util.NoSuchElementException;
import java.util.Scanner;

import com.codeaffine.gonsole.internal.resource.ConsoleIoProvider;

public class Input {

  private final ConsoleIoProvider consoleIOProvider;

  public Input( ConsoleIoProvider consoleIoProvider ) {
    this.consoleIOProvider = consoleIoProvider;
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