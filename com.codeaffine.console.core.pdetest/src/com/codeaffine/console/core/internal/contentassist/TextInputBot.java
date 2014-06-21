package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.line;

import org.eclipse.jface.text.ITextViewer;

import com.codeaffine.console.core.internal.Console;
import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.console.core.pdetest.console.TestConsoleDefinition;

class TextInputBot {

  private final ConsoleBot consoleBot;
  private final Console console;

  TextInputBot( ConsoleBot consoleBot ) {
    this.consoleBot = consoleBot;
    this.console = consoleBot.open( new TestConsoleDefinition() );
  }

  ITextViewer performTextInput( String text ) {
    consoleBot.typeText( text );
    return console.getPage().getViewer();
  }

  static int offset( int promptOffSet ) {
    return line( "" ).length() + promptOffSet;
  }
}