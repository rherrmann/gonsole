package com.codeaffine.console.core.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTEventHelper;

public class ActionKeyBindingManagerPDETest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ActionKeyBindingManager actionKeyBindingManager;
  private TextViewer textViewer;
  private IAction action;

  @Test
  public void testInitialActiveKeySequence() {
    KeyStroke activationKeyStroke = actionKeyBindingManager.getActiveKeySequence();

    assertThat( activationKeyStroke ).isNull();
  }

  @Test
  public void testPressMatchingKeySequence() {
    actionKeyBindingManager.addKeyBinding( ctrlSpace(), action );

    simulateKeyDown( SWT.CTRL, SWT.SPACE );

    verify( action ).run();
    assertThat( actionKeyBindingManager.getActiveKeySequence() ).isEqualTo( ctrlSpace() );
  }

  @Test
  public void testPressNonMatchingKeySequence() {
    actionKeyBindingManager.addKeyBinding( ctrlSpace(), action );

    simulateKeyDown( SWT.NONE, SWT.CR );

    verify( action, never() ).run();
    assertThat( actionKeyBindingManager.getActiveKeySequence() ).isNull();
  }

  @Test
  public void testPressNonMatchingAfterMatchingKeySequence() {
    actionKeyBindingManager.addKeyBinding( ctrlSpace(), action );

    simulateKeyDown( SWT.CTRL, SWT.SPACE );
    simulateKeyDown( SWT.NONE, 'x' );

    verify( action ).run();
    assertThat( actionKeyBindingManager.getActiveKeySequence() ).isEqualTo( ctrlSpace() );
  }

  @Before
  public void setUp() {
    textViewer = new TextViewer( displayHelper.createShell(), SWT.NONE );
    actionKeyBindingManager = new ActionKeyBindingManager();
    actionKeyBindingManager.activateFor( textViewer );
    action = mock( IAction.class );
  }

  private static KeyStroke ctrlSpace() {
    return KeyStroke.getInstance( SWT.MOD1, SWT.SPACE );
  }

  private void simulateKeyDown( int modifiers, char keyCode ) {
    SWTEventHelper.trigger( SWT.KeyDown )
      .withStateMask( modifiers )
      .withKeyCode( keyCode )
      .on( textViewer.getControl() );
  }
}
