package com.codeaffine.gonsole.internal.interpreter;

import java.util.ResourceBundle;

import org.eclipse.jgit.pgm.internal.CLIText;

@SuppressWarnings("restriction")
public class PgmResourceBundle {

  public ResourceBundle getResourceBundle() {
    return CLIText.get().resourceBundle();
  }

}