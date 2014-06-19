package com.codeaffine.gonsole.pdetest;

import java.util.Locale;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DefaultLocaleRule implements TestRule {

  private final Locale defaultLocale;

  public DefaultLocaleRule( Locale defaultLocale ) {
    this.defaultLocale = defaultLocale;
  }

  @Override
  public Statement apply( Statement base, Description description ) {
    return new DefaultLocaleStatement( base, defaultLocale );
  }
}