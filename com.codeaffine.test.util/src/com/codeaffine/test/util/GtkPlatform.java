package com.codeaffine.test.util;

import org.eclipse.swt.SWT;

import com.codeaffine.test.util.ConditionalIgnoreRule.IgnoreCondition;

public class GtkPlatform implements IgnoreCondition {
  @Override
  public boolean isSatisfied() {
    return "gtk".equals( SWT.getPlatform() );
  }
}

