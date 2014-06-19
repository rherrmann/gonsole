package com.codeaffine.console.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class DummyPDETest {

  @Test
  public void testName() {
    assertThat( new Dummy() ).isNotNull();
  }
}
