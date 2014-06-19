package com.codeaffine.console.core;

import org.eclipse.swt.graphics.RGB;

public interface ColorScheme {
  RGB getInputColor();
  RGB getOutputColor();
  RGB getPromptColor();
  RGB getErrorColor();
}
