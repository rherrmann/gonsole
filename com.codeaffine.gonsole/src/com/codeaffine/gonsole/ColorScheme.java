package com.codeaffine.gonsole;

import org.eclipse.swt.graphics.RGB;

public interface ColorScheme {
  RGB getInputColor();
  RGB getOutputColor();
  RGB getPromptColor();
  RGB getErrorColor();
}
