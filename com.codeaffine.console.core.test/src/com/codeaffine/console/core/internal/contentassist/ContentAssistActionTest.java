package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.ContentAssistAction.ACTION_DEFINITION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class ContentAssistActionTest {

  private ContentAssistAction action;
  private Editor editor;

  @Test
  public void testInitialization() {
    assertThat( action.getActionDefinitionId() ).isEqualTo( ACTION_DEFINITION_ID );
    assertThat( action.isEnabled() ).isTrue();
  }

  @Test
  public void testRun() {
    action.run();

    verify( editor ).fireDocumentChange();
    verify( editor ).showPossibleCompletions();
  }

  @Test
  public void testRunIfCaretWithinDocument() {
    when( editor.getDocumentLength() ).thenReturn( 10 );

    action.run();

    verify( editor, never() ).fireDocumentChange();
    verify( editor ).showPossibleCompletions();
  }

  @Test
  public void testRunWithInputPartition() {
    when( editor.getPartitionType() ).thenReturn( PartitionType.INPUT );

    action.run();

    verify( editor, never() ).fireDocumentChange();
    verify( editor ).showPossibleCompletions();
  }

  @Test
  public void testRunIfChangeNotAllowed() {
    when( editor.isDocumentChangeAllowed() ).thenReturn( false );

    action.run();

    verify( editor, never() ).fireDocumentChange();
    verify( editor ).showPossibleCompletions();
  }

  @Before
  public void setUp() {
    editor = stubEditor();
    action = new ContentAssistAction( editor );
  }

  private static Editor stubEditor() {
    Editor result = mock( Editor.class );
    when( result.getCaretOffset() ).thenReturn( 0 );
    when( result.getDocumentLength() ).thenReturn( 0 );
    when( result.getPartitionType() ).thenReturn( PartitionType.OUTPUT );
    when( result.isDocumentChangeAllowed() ).thenReturn( true );
    return result;
  }
}