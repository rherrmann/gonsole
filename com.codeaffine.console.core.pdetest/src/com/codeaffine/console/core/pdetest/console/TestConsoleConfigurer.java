package com.codeaffine.console.core.pdetest.console;

import com.codeaffine.console.core.Console;
import com.codeaffine.console.core.ConsoleConfigurer;
import com.codeaffine.console.core.pdetest.TestImageDescriptor;

public class TestConsoleConfigurer implements ConsoleConfigurer {

  public static final String TITLE = "Test Console";
  public static final TestImageDescriptor IMAGE = new TestImageDescriptor();

  @Override
  public void configure( Console console ) {
    console.setTitle( TITLE );
    console.setImageDescriptor( IMAGE );
    console.setConsoleComponentFactory( new TestConsoleComponentFactory() );
    console.setColorScheme( new TestColorScheme() );
  }
}