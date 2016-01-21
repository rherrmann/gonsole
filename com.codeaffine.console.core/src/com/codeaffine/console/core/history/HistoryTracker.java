package com.codeaffine.console.core.history;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.Proposal;


public class HistoryTracker implements ConsoleCommandInterpreter, ContentProposalProvider {

  static final KeyStroke ACTIVATION_KEY_STROKE = KeyStroke.getInstance( SWT.ARROW_UP );

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
    List<String> items = new ArrayList<>( Arrays.asList( historyStore.getItems() ) );
    items.remove( newItem );
    items.add( 0, newItem );
    historyStore.setItems( items.stream().limit( itemLimit ).toArray( String[]::new ) );
    return false;
  }

  @Override
  public String execute( String... commandLine ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Proposal[] getContentProposals() {
    return getHistoryItems().stream().map( new java.util.function.Function<String, Proposal>() {
      private int sortKey;
      @Override
      public Proposal apply( String historyItem ) {
        sortKey++;
        return new Proposal( Integer.valueOf( sortKey ), historyItem, null, imageDescriptor );
      }
    } ).toArray( Proposal[]::new );
  }

  @Override
  public KeyStroke getActivationKeySequence() {
    return ACTIVATION_KEY_STROKE;
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
