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
import com.codeaffine.console.core.internal.ConsoleEditor;
import com.codeaffine.test.util.swt.DisplayHelper;

public class ContentAssistTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ConsoleEditor consoleEditor;
  private ContentAssistant contentAssistant;
  private ContentAssist contentAssist;

  @Test
  public void testShowPossibeleCompletionsIfCaretWithinDocument() {
    when( consoleEditor.getDocumentLength() ).thenReturn( 10 );

    contentAssist.showPossibleCompletions();

    verify( consoleEditor, never() ).fireDocumentChange();
    verify( contentAssistant ).showPossibleCompletions();
  }

  @Test
  public void testShowPossibeleCompletionsWithInputPartition() {
    when( consoleEditor.getPartitionType() ).thenReturn( PartitionType.INPUT );

    contentAssist.showPossibleCompletions();

    verify( consoleEditor, never() ).fireDocumentChange();
    verify( contentAssistant ).showPossibleCompletions();
  }

  @Test
  public void testShowPossibeleCompletionsIfChangeNotAllowed() {
    when( consoleEditor.isDocumentChangeAllowed() ).thenReturn( false );

    contentAssist.showPossibleCompletions();

    verify( consoleEditor, never() ).fireDocumentChange();
    verify( contentAssistant ).showPossibleCompletions();
  }

  @Before
  public void setUp() {
    consoleEditor = stubEditor();
    contentAssistant = mock( ContentAssistant.class );
    ConsoleComponentFactory consoleComponentFactory = mock( ConsoleComponentFactory.class );
    contentAssist = new ContentAssist( consoleEditor, contentAssistant, consoleComponentFactory );
  }

  private ConsoleEditor stubEditor() {
    ConsoleEditor result = mock( ConsoleEditor.class );
    when( result.getCaretOffset() ).thenReturn( 0 );
    when( result.getDocumentLength() ).thenReturn( 0 );
    when( result.getPartitionType() ).thenReturn( PartitionType.OUTPUT );
    when( result.isDocumentChangeAllowed() ).thenReturn( true );
    when( result.isCaretInLastInputPartition() ).thenReturn( true );
    when( result.getDisplay() ).thenReturn( displayHelper.getDisplay() );
    return result;
  }
}
