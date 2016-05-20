package com.codeaffine.gonsole.internal.contentassist;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jgit.pgm.CommandCatalog;
import org.eclipse.jgit.pgm.CommandRef;

public class CommandRefCollector {
  private final Set<CommandRef> commandRefs;

  public CommandRefCollector() {
    commandRefs = new HashSet<>();
  }

  public Collection<CommandRef> collect() {
    Collections.addAll( commandRefs, CommandCatalog.common() );
    addExtraCommandRef( "remote" );
    addExtraCommandRef( "blame" );
    addExtraCommandRef( "merge-base" );
    addExtraCommandRef( "rev-parse" );
    return commandRefs;
  }

  private void addExtraCommandRef( String commandName ) {
    CommandRef commandRef = CommandCatalog.get( commandName );
    if( commandRef != null ) {
      commandRefs.add( commandRef );
    }
  }
}