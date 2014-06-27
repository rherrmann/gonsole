package com.codeaffine.console.core.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class GenericConsolePageParticipantPDETest {

  @Test
  public void testDefaultConstructor() throws Exception {
    GenericConsolePageParticipant instance = GenericConsolePageParticipant.class.newInstance();

    assertThat( instance ).isNotNull();
  }
}
