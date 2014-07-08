package com.codeaffine.console.core.history;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Iterables.limit;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.ObjectArrays.concat;
import static java.util.Arrays.asList;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.google.common.base.Function;


public class HistoryTracker implements ConsoleCommandInterpreter, ContentProposalProvider {

  static final String ACTIVATION_KEY_SEQUENCE = "Arrow_Up";

  private final int limit;
  private final ImageDescriptor imageDescriptor;
  private final HistoryStore historyStore;

  public HistoryTracker( int limit, ImageDescriptor imageDescriptor, HistoryStore historyStore ) {
    this.limit = limit;
    this.imageDescriptor = imageDescriptor;
    this.historyStore = historyStore;
  }

  public void clear() {
    historyStore.clearItems();
  }

  @Override
  public boolean isRecognized( String... commandLine ) {
    String newItem = on( ' ' ).join( commandLine );
    historyStore.setItems( limitItems( concat( newItem, historyStore.getItems() ) ));
    return false;
  }

  @Override
  public String execute( String... commandLine ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Proposal[] getContentProposals() {
    Iterable<Proposal> proposals = transform( getHistoryItems(), new Function<String,Proposal>() {
      int sortKey;
      @Override
      public Proposal apply( String input ) {
        sortKey++;
        return new Proposal( Integer.valueOf( sortKey ), input, null, imageDescriptor );
      }
    } );
    return toArray( proposals, Proposal.class );
  }

  @Override
  public String getActivationKeySequence() {
    return ACTIVATION_KEY_SEQUENCE;
  }

  private String[] limitItems( String[] items ) {
    return toArray( limit( asList( items ), limit ), String.class );
  }

  private List<String> getHistoryItems() {
    return asList( historyStore.getItems() );
  }
}
