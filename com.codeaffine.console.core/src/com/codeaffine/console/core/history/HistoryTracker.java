package com.codeaffine.console.core.history;

import static com.google.common.collect.Iterables.limit;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;
import com.google.common.base.Function;


public class HistoryTracker implements ConsoleCommandInterpreter, ContentProposalProvider {

  static final String ACTIVATION_KEY_SEQUENCE = "Arrow_Up";

  private final int itemLimit;
  private final ImageDescriptor imageDescriptor;
  private final HistoryStore historyStore;

  public HistoryTracker( int limit, ImageDescriptor imageDescriptor, HistoryStore historyStore ) {
    this.itemLimit = limit;
    this.imageDescriptor = imageDescriptor;
    this.historyStore = historyStore;
  }

  public void clear() {
    historyStore.clearItems();
  }

  @Override
  public boolean isRecognized( String... commandLine ) {
    String newItem = joinCommandLine( commandLine );
    List<String> items = newArrayList( historyStore.getItems() );
    items.remove( newItem );
    items.add( 0, newItem );
    historyStore.setItems( toArray( limit( items, itemLimit ), String.class ) );
    return false;
  }

  @Override
  public String execute( String... commandLine ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Proposal[] getContentProposals() {
    Iterable<Proposal> proposals = transform( getHistoryItems(), new Function<String,Proposal>() {
      private int sortKey;
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

  private List<String> getHistoryItems() {
    return asList( historyStore.getItems() );
  }

  private static String joinCommandLine( String... commandLine ) {
    StringBuilder builder = new StringBuilder();
    for( String command : commandLine ) {
      if( builder.length() > 0 ) {
        builder.append( ' ' );
      }
      boolean containsWhitespace = containsWhitespace( command );
      if( containsWhitespace ) {
        builder.append( '"' );
      }
      builder.append( command );
      if( containsWhitespace ) {
        builder.append( '"' );
      }
    }
    return builder.toString();
  }

  private static boolean containsWhitespace( String string ) {
    Pattern whitespacePattern = Pattern.compile( "\\s" );
    return whitespacePattern.matcher( string ).find();
  }
}
