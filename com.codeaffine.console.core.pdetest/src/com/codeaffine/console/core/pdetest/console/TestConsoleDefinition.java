package com.codeaffine.console.core.pdetest.console;

import java.nio.charset.Charset;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

import com.codeaffine.console.core.ColorScheme;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleDefinition;
import com.google.common.base.Charsets;

public class TestConsoleDefinition implements ConsoleDefinition {

  public static ImageDescriptor IMAGE = new ImageDescriptor() {
    @Override
    public ImageData getImageData() {
      return new ImageData(6, 6, 1, new PaletteData(new RGB[] { new RGB(255, 0, 0) }));
    }
  };

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
    return IMAGE;
  }

  @Override
  public Charset getEncoding() {
    return Charsets.UTF_8;
  }

  @Override
  public ColorScheme getColorScheme() {
    return new TestColorScheme();
  }

  @Override
  public ConsoleComponentFactory getConsoleComponentFactory() {
    return new TestConsoleComponentFactory();
  }
}