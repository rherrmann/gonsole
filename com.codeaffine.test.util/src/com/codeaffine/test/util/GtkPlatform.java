package com.codeaffine.test.util;

import com.codeaffine.test.util.ConditionalIgnoreRule.IgnoreCondition;

public class GtkPlatform implements IgnoreCondition {
  @Override
  public boolean isSatisfied() {
    return false;
  }
}

