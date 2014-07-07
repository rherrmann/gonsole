package com.codeaffine.console.core.internal.contentassist;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.test.util.swt.DisplayHelper;

public class ContentAssistTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Editor editor;
  private ContentAssistant contentAssistant;
  private ContentAssist contentAssist;

  @Test
  public void testShowPossibeleCompletionsIfCaretWithinDocument() {
    when( editor.getDocumentLength() ).thenReturn( 10 );

    contentAssist.showPossibleCompletions();

    verify( editor, never() ).fireDocumentChange();
    verify( contentAssistant ).showPossibleCompletions();
  }

  @Test
  public void testShowPossibeleCompletionsWithInputPartition() {
    when( editor.getPartitionType() ).thenReturn( PartitionType.INPUT );

    contentAssist.showPossibleCompletions();

    verify( editor, never() ).fireDocumentChange();
    verify( contentAssistant ).showPossibleCompletions();
  }

  @Test
  public void testShowPossibeleCompletionsIfChangeNotAllowed() {
    when( editor.isDocumentChangeAllowed() ).thenReturn( false );

    contentAssist.showPossibleCompletions();

    verify( editor, never() ).fireDocumentChange();
    verify( contentAssistant ).showPossibleCompletions();
  }

  @Before
  public void setUp() {
    editor = stubEditor();
    contentAssistant = mock( ContentAssistant.class );
    ConsoleComponentFactory consoleComponentFactory = mock( ConsoleComponentFactory.class );
    contentAssist = new ContentAssist( editor, contentAssistant, consoleComponentFactory );
  }

  private Editor stubEditor() {
    Editor result = mock( Editor.class );
    when( result.getCaretOffset() ).thenReturn( 0 );
    when( result.getDocumentLength() ).thenReturn( 0 );
    when( result.getPartitionType() ).thenReturn( PartitionType.OUTPUT );
    when( result.isDocumentChangeAllowed() ).thenReturn( true );
    when( result.isCaretInLastInputPartition() ).thenReturn( true );
    when( result.getDisplay() ).thenReturn( displayHelper.getDisplay() );
    return result;
  }
}
