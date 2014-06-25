package com.codeaffine.console.calculator.internal;

import com.codeaffine.console.core.Console;
import com.codeaffine.console.core.ConsoleConfigurer;

public class CalculatorConsoleConfigurer implements ConsoleConfigurer {

  @Override
  public void configure( Console console ) {
    console.setTitle( "Calculator" );
    console.setColorScheme( new CalculatorColorScheme() );
    console.setConsoleComponentFactory( new CalculatorComponentFactory() );
  }
}