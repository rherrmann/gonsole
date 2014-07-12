package com.codeaffine.gonsole.internal;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.themes.ITheme;

import com.codeaffine.console.core.ColorScheme;

class GitConsoleColorScheme implements ColorScheme {

  @Override
  public RGB getInputColor() {
    return getColorRegistry().get( "com.codeaffine.gonsole.internal.InputColor" ).getRGB();
  }

  @Override
  public RGB getOutputColor() {
    return getColorRegistry().get( "com.codeaffine.gonsole.internal.OutputColor" ).getRGB();
  }

  @Override
  public RGB getPromptColor() {
    return getColorRegistry().get( "com.codeaffine.gonsole.internal.PromptColor" ).getRGB();
  }

  @Override
  public RGB getErrorColor() {
    return getColorRegistry().get( "com.codeaffine.gonsole.internal.ErrorColor" ).getRGB();
  }

  private static ColorRegistry getColorRegistry() {
    return getCurrentTheme().getColorRegistry();
  }

  private static ITheme getCurrentTheme() {
    return PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
  }

}