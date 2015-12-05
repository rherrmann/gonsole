package com.codeaffine.console.core;

import java.util.stream.Stream;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;


public class Consoles {

  public static Console[] listConsoles() {
    IConsole[] allConsoles = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
    return Stream.of( allConsoles )
      .filter( console -> console instanceof Console )
      .map( console -> ( Console )console )
      .toArray( Console[]::new );
  }

  private Consoles() { }
}
