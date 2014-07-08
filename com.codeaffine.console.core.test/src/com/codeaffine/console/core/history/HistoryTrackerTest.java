package com.codeaffine.console.core.history;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.console.core.Proposal;
import com.codeaffine.console.core.history.HistoryStore;
import com.codeaffine.console.core.history.HistoryTracker;
import com.codeaffine.console.core.test.TestImageDescriptor;

public class HistoryTrackerTest {

  private static final String[] ARBITRARY_STRINGS = new String[] { "", "", "" };
  private HistoryTracker historyTracker;
  private TestImageDescriptor imageDescriptor;
  private HistoryStore historyStore;

  @Test
  public void testPrependItemToEmptyHistory() {
    historyTracker.isRecognized( "command", "param" );

    verify( historyStore ).setItems( "command param" );
  }

  @Test
  public void testPrependItemToExistingHistory() {
    when( historyStore.getItems() ).thenReturn( new String[] { "command1" } );

    historyTracker.isRecognized( "command2");

    verify( historyStore ).setItems( "command2", "command1" );
  }

  @Test
  public void testPrependItemWithOverflow() {
    when( historyStore.getItems() ).thenReturn( new String[] { "command2", "command1" } );

    historyTracker.isRecognized( "command3");

    verify( historyStore ).setItems( "command3", "command2" );
  }

  @Test
  public void testGetItemsFromHistory() {
    when( historyStore.getItems() ).thenReturn( new String[] { "command" } );

    Proposal[] contentProposals = historyTracker.getContentProposals();

    assertThat( contentProposals ).hasSize( 1 );
    assertThat( contentProposals[ 0 ].getText() ).isEqualTo( "command" );
    assertThat( contentProposals[ 0 ].getInfo() ).isNull();
    assertThat( contentProposals[ 0 ].getImageDescriptor() ).isEqualTo( imageDescriptor );
  }

  @Test
  public void testSortKeyOfHistory() {
    when( historyStore.getItems() ).thenReturn( new String[] { "command2", "command1" } );

    Proposal[] contentProposals = historyTracker.getContentProposals();

    assertThat( contentProposals ).hasSize( 2 );
    assertThat( contentProposals[ 0 ].getSortKey() ).isEqualTo( 1 );
    assertThat( contentProposals[ 1 ].getSortKey() ).isEqualTo( 2 );
  }

  @Test
  public void testIsRecognized() {
    boolean recognized = historyTracker.isRecognized( ARBITRARY_STRINGS );

    assertThat( recognized ).isFalse();
  }

  @Test(expected=UnsupportedOperationException.class)
  public void testExecute() {
    historyTracker.execute( ARBITRARY_STRINGS );
  }

  @Test
  public void testGetItemsActivationKeySequence() {
    String activationKeySequence = historyTracker.getActivationKeySequence();

    assertThat( activationKeySequence ).isEqualTo( HistoryTracker.ACTIVATION_KEY_SEQUENCE );
  }

  @Test
  public void testClear() {
    historyTracker.clear();

    verify( historyStore ).clearItems();
  }

  @Before
  public void setUp() {
    imageDescriptor = new TestImageDescriptor();
    historyStore = mock( HistoryStore.class );
    when( historyStore.getItems() ).thenReturn( new String[ 0 ] );
    historyTracker = new HistoryTracker( 2, imageDescriptor, historyStore );
  }
}
