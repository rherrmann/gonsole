package com.codeaffine.console.calculator.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;



public class DummyPDETest {

  @Test
  public void testFirst() {
   assertThat( new Dummy() ).isNotNull();
  }
}
