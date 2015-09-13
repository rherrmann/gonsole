package com.codeaffine.console.core.internal;

import static com.codeaffine.console.core.ConsoleConstants.ENCODING;

import java.util.NoSuchElementException;
import java.util.Scanner;

import com.codeaffine.console.core.internal.resource.ConsoleIoProvider;

public class Input {

  private final ConsoleIoProvider consoleIOProvider;

  public Input( ConsoleIoProvider consoleIoProvider ) {
    this.consoleIOProvider = consoleIoProvider;
  }

  @SuppressWarnings("resource")
  public String readLine() {
    Scanner scanner = new Scanner( consoleIOProvider.getInputStream(), ENCODING.name() );
    String result;
    try {
      result = scanner.nextLine();
    } catch( NoSuchElementException endOfFile ) {
      result = null;
    }
    return result;
  }
}