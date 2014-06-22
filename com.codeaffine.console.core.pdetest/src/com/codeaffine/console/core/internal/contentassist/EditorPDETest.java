package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.offset;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.internal.Console;
import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.console.core.pdetest.console.TestConsoleDefinition;

public class EditorPDETest {

  private static final String TEXT = "text";

  @Rule public final ConsoleBot console = new ConsoleBot();

  private ConsoleContentAssist contentAssistant;
  private ContentAssistActivation activation;
  private Editor editor;

  @Test
  public void testGetCaretOffset() {
    console.typeText( TEXT );

    int actual = editor.getCaretOffset();

    assertThat( actual ).isEqualTo( offset( TEXT.length() ) );
  }

  @Test
  public void testGetDocumentLength() {
    console.typeText( TEXT );

    int actual = editor.getDocumentLength();

    assertThat( actual ).isEqualTo( offset( TEXT.length() ) );
  }

  @Test
  public void testGetPartitionTypeWithoutInput() {
    String actual = editor.getPartitionType();

    assertThat( actual ).isEqualTo( PartitionType.OUTPUT );
  }

  @Test
  public void testGetPartitionTypeAfterInput() {
    console.typeText( TEXT );

    String actual = editor.getPartitionType();

    assertThat( actual ).isEqualTo( PartitionType.INPUT );
  }

  @Test
  public void testIsDocumentChangeAllowed() {
    boolean actual = editor.isDocumentChangeAllowed();

    assertThat( actual ).isTrue();
  }

  @Test
  public void testFireDocumentChange() {
    editor.fireDocumentChange();

    assertThat( editor.getPartitionType() ).isEqualTo( PartitionType.INPUT );
  }

  @Test
  public void testShowPossibleCompletions() {
    editor.showPossibleCompletions();

    verify( contentAssistant ).showPossibleCompletions();
  }

  @Test
  public void testFocusGained() {
    editor.focusGained( null );

    verify( activation ).activate();
  }

  @Test
  public void testFocusLost() {
    editor.focusLost( null );

    verify( activation ).deactivate();
  }

  @Before
  public void setUp() {
    contentAssistant = mock( ConsoleContentAssist.class );
    activation = mock( ContentAssistActivation.class );
    editor = new Editor( openConsole().getPage().getViewer(), contentAssistant, activation );
  }

  private Console openConsole() {
    return console.open( new TestConsoleDefinition() );
  }
}