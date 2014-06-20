package com.codeaffine.gonsole.internal;

import org.eclipse.swt.widgets.Display;

import com.codeaffine.console.core.ConsoleDefinition;
import com.codeaffine.console.core.ConsoleFactory;

public class GitConsoleFactory extends ConsoleFactory {

  @Override
  protected ConsoleDefinition getConsoleDefinition() {
    return new GitConsoleDefinition( Display.getCurrent() );
  }
}