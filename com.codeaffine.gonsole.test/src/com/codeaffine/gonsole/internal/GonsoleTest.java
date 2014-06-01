package com.codeaffine.gonsole.internal;

import static org.junit.Assert.assertTrue;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class GonsoleTest {

  @Test
  public void testPlain() {
    assertTrue( true );
  }

  @Test
  @Parameters( value = {"1", "2", "3" })
  public void testParameterized(int value) throws Exception {
    System.out.println(value);
  }
}
