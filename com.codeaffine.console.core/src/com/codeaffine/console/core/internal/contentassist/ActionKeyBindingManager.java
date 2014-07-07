package com.codeaffine.console.core.internal.contentassist;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Event;


public class ActionKeyBindingManager {

  private final Map<KeyStroke,IAction> keyBindings;
  private String activeKeySequence;

  public ActionKeyBindingManager() {
    keyBindings = newHashMap();
  }

  public void addKeyBinding( String keySequence, IAction action ) {
    keyBindings.put( translateKeySequence( keySequence ), action );
  }

  public void activateFor( ITextViewerExtension textViewer ) {
    textViewer.prependVerifyKeyListener( new VerifyKeyListener() {
      @Override
      public void verifyKey( VerifyEvent verifyEvent ) {
        handleVerifyKeyEvent( verifyEvent );
      }
    } );
  }

  public String getActiveKeySequence() {
    return activeKeySequence;
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
      activeKeySequence = keyStroke.toString();
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

  private static KeyStroke translateKeySequence( String string ) {
    try {
      return KeyStroke.getInstance( string );
    } catch( ParseException pe ) {
      throw new IllegalArgumentException( "Invaid keystroke: " + string, pe );
    }
  }

}
