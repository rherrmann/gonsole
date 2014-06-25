package com.codeaffine.gonsole.internal;

import org.eclipse.swt.widgets.Display;

import com.codeaffine.console.core.ConsoleConfigurer;
import com.codeaffine.console.core.ConsoleFactory;

public class GitConsoleFactory extends ConsoleFactory {

  @Override
  protected ConsoleConfigurer getConsoleDefinition() {
    return new GitConsoleConfigurer( Display.getCurrent() );
  }
}