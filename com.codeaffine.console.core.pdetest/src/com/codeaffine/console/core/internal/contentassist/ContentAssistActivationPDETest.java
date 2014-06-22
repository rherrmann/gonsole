package com.codeaffine.console.core.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.eclipse.core.commands.NotHandledException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ContentAssistActivationPDETest {

  private ContentAssistActivation actionActivation;
  private TestAction action;

  @Test
  public void testActivate() throws Exception {
    actionActivation.activate();
    executeAction();

    assertThat( action.isExecuted() ).isTrue();
    assertThat( actionActivation.isActive() ).isTrue();
  }

  @Test
  public void testDeactivate() throws Exception {
    actionActivation.activate();

    try {
      actionActivation.deactivate();
      executeAction();
      fail();
    } catch( NotHandledException expected ) {
    }

    assertThat( actionActivation.isActive() ).isFalse();
  }

  @Test
  public void testReactivation() throws Exception {
    actionActivation.activate();
    actionActivation.deactivate();
    actionActivation.activate();
    executeAction();

    assertThat( action.isExecuted() ).isTrue();
  }

  @Test( expected = IllegalStateException.class )
  public void testActivateTwice() {
    actionActivation.activate();
    actionActivation.activate();
  }

  @Test(  expected = IllegalStateException.class )
  public void testDeactivateIfNotActive() {
    actionActivation.deactivate();
  }

  @Before
  public void setUp() {
    action = new TestAction();
    actionActivation = new ContentAssistActivation( action );
  }

  @After
  public void tearDown() {
    if( actionActivation.isActive() ) {
      actionActivation.deactivate();
    }
  }

  private static void executeAction() throws Exception {
    IHandlerService service = ( IHandlerService )PlatformUI.getWorkbench().getService( IHandlerService.class );
    service.executeCommand( TestAction.COMMAND_ID, null );
  }
}