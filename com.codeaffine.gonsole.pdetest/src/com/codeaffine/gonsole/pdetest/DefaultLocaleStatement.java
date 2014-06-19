package com.codeaffine.gonsole.pdetest;

import java.util.Locale;

import org.junit.runners.model.Statement;

class DefaultLocaleStatement extends Statement {

  private final Locale defaultLocale;
  private final Statement base;

  private Locale locale;

  DefaultLocaleStatement( Statement base, Locale defaultLocale  ) {
    this.defaultLocale = defaultLocale;
    this.base = base;
  }

  @Override
  public void evaluate() throws Throwable {
    try {
      locale = Locale.getDefault();
      Locale.setDefault( defaultLocale );
      base.evaluate();
    } finally {
      Locale.setDefault( locale );
    }
  }
}