package com.codeaffine.gonsole.pdetest;

import static com.codeaffine.gonsole.internal.activator.IconRegistry.GONSOLE;

import java.nio.charset.Charset;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.RGB;

import com.codeaffine.gonsole.ColorScheme;
import com.codeaffine.gonsole.ConsoleCommandInterpreter;
import com.codeaffine.gonsole.ConsoleComponentFactory;
import com.codeaffine.gonsole.ConsoleDefinition;
import com.codeaffine.gonsole.ConsoleOutput;
import com.codeaffine.gonsole.ConsolePrompt;
import com.codeaffine.gonsole.ContentProposalProvider;
import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.google.common.base.Charsets;

public class TestConsoleDefinition implements ConsoleDefinition {

  @Override
  public String getType() {
    return "test.console";
  }

  @Override
  public String getTitle() {
    return "Test Console";
  }

  @Override
  public ImageDescriptor getImage() {
    return new IconRegistry().getDescriptor( GONSOLE );
  }

  @Override
  public Charset getEncoding() {
    return Charsets.UTF_8;
  }

  @Override
  public ColorScheme getColorScheme() {
    return new ColorScheme() {

      @Override
      public RGB getPromptColor() {
        return new RGB( 128, 128, 128 );
      }

      @Override
      public RGB getOutputColor() {
        return new RGB( 255, 255, 255 );
      }

      @Override
      public RGB getInputColor() {
        return new RGB( 0, 0, 255 );
      }

      @Override
      public RGB getErrorColor() {
        return new RGB( 255, 0, 0 );
      }
    };
  }

  @Override
  public ConsoleComponentFactory getConsoleComponentFactory() {
    return new ConsoleComponentFactory() {

      @Override
      public ConsolePrompt createConsolePrompt( ConsoleOutput consoleOutput ) {
        return null;
      }

      @Override
      public ConsoleCommandInterpreter[] createCommandInterpreters( ConsoleOutput consoleOutput ) {
        return null;
      }

      @Override
      public ContentProposalProvider[] createProposalProviders() {
        return null;
      }
    };
  }
}