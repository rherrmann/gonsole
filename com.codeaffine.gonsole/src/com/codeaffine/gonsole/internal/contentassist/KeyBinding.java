package com.codeaffine.gonsole.internal.contentassist;

import static org.eclipse.ui.IWorkbenchCommandConstants.EDIT_CONTENT_ASSIST;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.commands.contexts.ContextManager;
import org.eclipse.jface.bindings.BindingManager;
import org.eclipse.jface.bindings.Scheme;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.keys.IBindingService;


class KeyBinding {

  private final ICommandService commandService;
  private final IBindingService bindingService;
  private final BindingManager localBindingManager;

  KeyBinding() {
    commandService = getService( ICommandService.class );
    bindingService = getService( IBindingService.class );
    localBindingManager = new BindingManager( new ContextManager(), new CommandManager() );
    initializeLocalBindingManager();
  }

  String getContentAssistKeyBinding() {
    TriggerSequence[] triggerSequences = getTriggerSequences();
    return toString( triggerSequences );
  }

  private void initializeLocalBindingManager() {
    Scheme[] definedSchemes = bindingService.getDefinedSchemes();
    if( definedSchemes != null ) {
      try {
        for( Scheme scheme : definedSchemes ) {
          Scheme localScheme = localBindingManager.getScheme( scheme.getId() );
          localScheme.define( scheme.getName(), scheme.getDescription(), scheme.getParentId() );
        }
      } catch( NotDefinedException ignore ) {
      }
    }
    localBindingManager.setLocale( bindingService.getLocale() );
    localBindingManager.setPlatform( bindingService.getPlatform() );
    localBindingManager.setBindings( bindingService.getBindings() );
    try {
      Scheme activeScheme = bindingService.getActiveScheme();
      if( activeScheme != null ) {
        localBindingManager.setActiveScheme( activeScheme );
      }
    } catch( NotDefinedException ignore ) {
    }
  }

  private TriggerSequence[] getTriggerSequences() {
    Command command = commandService.getCommand( EDIT_CONTENT_ASSIST );
    ParameterizedCommand parameterizedCommand = new ParameterizedCommand( command, null );
    return localBindingManager.getActiveBindingsDisregardingContextFor( parameterizedCommand );
  }

  private static String toString( TriggerSequence[] triggerSequences ) {
    StringBuffer buffer = new StringBuffer();
    for( TriggerSequence triggerSequence : triggerSequences ) {
      if( buffer.length() > 0 ) {
        buffer.append( ' ' );
      }
      buffer.append( triggerSequence.format() );
    }
    return buffer.toString();
  }

  @SuppressWarnings("unchecked")
  private static <T> T getService( Class<T> type ) {
    return ( T )PlatformUI.getWorkbench().getService( type );
  }
}
