package com.codeaffine.console.core.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.eclipse.core.commands.NotHandledException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ActionActivationPDETest {

  private ActionActivation actionActivation;

  @Test
  public void testActivate() throws Exception {
    TestAction action = new TestAction();

    actionActivation.activate( action );
    executeAction();

    assertThat( action.isExecuted() ).isTrue();
  }

  @Test
  public void testActivateTwice() throws Exception {
    TestAction action2 = new TestAction();
    TestAction action1 = new TestAction();

    actionActivation.activate( action1 );
    actionActivation.activate( action2 );
    executeAction();

    assertThat( action1.isExecuted() ).isTrue();
    assertThat( action2.isExecuted() ).isFalse();
  }

  @Test
  public void testDeactivate() throws Exception {
    TestAction action = new TestAction();
    actionActivation.activate( action );

    try {
      actionActivation.deactivate();
      executeAction();
      fail();
    } catch( NotHandledException expected ) {
    }
  }

  @Test
  public void testReActivation() throws Exception {
    TestAction action = new TestAction();

    actionActivation.activate( action );
    actionActivation.deactivate();
    actionActivation.activate( action );
    executeAction();

    assertThat( action.isExecuted() ).isTrue();
  }

  @Before
  public void setUp() {
    actionActivation = new ActionActivation();
  }

  @After
  public void tearDown() {
    actionActivation.deactivate();
  }

  private static void executeAction() throws Exception {
    IHandlerService service = ( IHandlerService )PlatformUI.getWorkbench().getService( IHandlerService.class );
    service.executeCommand( TestAction.COMMAND_ID, null );
  }
}