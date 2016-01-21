package com.codeaffine.console.core.internal.contentassist;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Event;


public class ActionKeyBindingManager {

  private final Map<KeyStroke,IAction> keyBindings;
  private KeyStroke activeKeyStroke;

  public ActionKeyBindingManager() {
    keyBindings = new HashMap<>();
  }

  public void addKeyBinding( KeyStroke keyStroke, IAction action ) {
    keyBindings.put( keyStroke, action );
  }

  public void activateFor( ITextViewerExtension textViewer ) {
    textViewer.prependVerifyKeyListener( new VerifyKeyListener() {
      @Override
      public void verifyKey( VerifyEvent verifyEvent ) {
        handleVerifyKeyEvent( verifyEvent );
      }
    } );
  }

  public KeyStroke getActiveKeySequence() {
    return activeKeyStroke;
  }

  private void handleVerifyKeyEvent( VerifyEvent verifyEvent ) {
    Event event = new Event();
    event.character = verifyEvent.character;
    event.keyCode = verifyEvent.keyCode;
    event.stateMask = verifyEvent.stateMask;
    event.doit = verifyEvent.doit;
    handleKeyEvent( event );
    verifyEvent.doit = event.doit;
  }

  private void handleKeyEvent( Event event ) {
    KeyStroke keyStroke = keyStrokeFromUnmodifiedEvent( event );
    if( !keyBindings.containsKey( keyStroke ) ) {
      keyStroke = keyStrokeFromUnshiftedEvent( event );
    }
    if( !keyBindings.containsKey( keyStroke ) ) {
      keyStroke = keyStrokeFromModifierEvent( event );
    }
    if( keyBindings.containsKey( keyStroke ) ) {
      event.doit = false;
      activeKeyStroke = keyStroke;
      IAction action = keyBindings.get( keyStroke );
      action.run();
    }
  }

  private static KeyStroke keyStrokeFromUnmodifiedEvent( Event event ) {
    int accelerator = SWTKeySupport.convertEventToUnmodifiedAccelerator( event );
    return SWTKeySupport.convertAcceleratorToKeyStroke( accelerator );
  }

  private static KeyStroke keyStrokeFromUnshiftedEvent( Event event ) {
    int accelerator = SWTKeySupport.convertEventToUnshiftedModifiedAccelerator( event );
    return SWTKeySupport.convertAcceleratorToKeyStroke( accelerator );
  }

  private static KeyStroke keyStrokeFromModifierEvent( Event event ) {
    int accelerator = SWTKeySupport.convertEventToModifiedAccelerator( event );
    return SWTKeySupport.convertAcceleratorToKeyStroke( accelerator );
  }
}
