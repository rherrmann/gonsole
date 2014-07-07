package com.codeaffine.console.core.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class ContentAssistActionTest {

  private ContentAssistAction action;
  private ContentAssist contentAssist;

  @Test
  public void testInitialEnablement() {
    assertThat( action.isEnabled() ).isTrue();
  }

  @Test
  public void testRun() {
    action.run();

    verify( contentAssist ).showPossibleCompletions();
  }

  @Before
  public void setUp() {
    contentAssist = mock( ContentAssist.class );
    action = new ContentAssistAction( contentAssist );
  }
}