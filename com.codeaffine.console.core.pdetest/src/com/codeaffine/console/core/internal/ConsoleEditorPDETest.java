package com.codeaffine.console.core.internal;

import static com.codeaffine.console.core.pdetest.console.TestConsolePrompt.offset;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.internal.contentassist.PartitionType;
import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.console.core.pdetest.console.TestConsoleConfigurer;

public class ConsoleEditorPDETest {

  private static final String TEXT = "text";

  @Rule public final ConsoleBot console = new ConsoleBot();

  private ConsoleEditor consoleEditor;

  @Test
  public void testGetCaretOffset() {
    console.typeText( TEXT );

    int actual = consoleEditor.getCaretOffset();

    assertThat( actual ).isEqualTo( offset( TEXT.length() ) );
  }

  @Test
  public void testGetDocumentLength() {
    console.typeText( TEXT );

    int actual = consoleEditor.getDocumentLength();

    assertThat( actual ).isEqualTo( offset( TEXT.length() ) );
  }

  @Test
  public void testGetPartitionTypeWithoutInput() {
    String actual = consoleEditor.getPartitionType();

    assertThat( actual ).isEqualTo( PartitionType.OUTPUT );
  }

  @Test
  public void testGetPartitionTypeAfterInput() {
    console.typeText( TEXT );

    String actual = consoleEditor.getPartitionType();

    assertThat( actual ).isEqualTo( PartitionType.INPUT );
  }

  @Test
  public void testIsDocumentChangeAllowed() {
    boolean actual = consoleEditor.isDocumentChangeAllowed();

    assertThat( actual ).isTrue();
  }

  @Test
  public void testFireDocumentChange() {
    consoleEditor.fireDocumentChange();

    assertThat( consoleEditor.getPartitionType() ).isEqualTo( PartitionType.INPUT );
  }

  @Test
  public void testComputePrefixWithOffsetAtLineEnd() {
    console.typeText( TEXT );

    String actual = consoleEditor.computePrefix( offset( TEXT.length() ) );

    assertThat( actual ).isEqualTo( TEXT );
  }

  @Test
  public void testComputePrefixWithOffsetAtCommandStart() {
    console.typeText( TEXT );

    String actual = consoleEditor.computePrefix( offset( 0 ) );

    assertThat( actual ).isEqualTo( "" );
  }

  @Test
  public void testComputePrefixWithOffsetInText() {
    console.typeText( TEXT );

    String actual = consoleEditor.computePrefix( offset( 2 ) );

    assertThat( actual ).isEqualTo( TEXT.substring( 0, 2 ) );
  }

  @Test
  public void testComputePrefixWithOffsetInBadLocation() {
    console.typeText( TEXT );

    String actual = consoleEditor.computePrefix( offset( TEXT.length() + 1 ) );

    assertThat( actual ).isEqualTo( "" );
  }

  @Before
  public void setUp() {
    consoleEditor = new ConsoleEditor( openConsole().getPage().getViewer() );
  }

  private GenericConsole openConsole() {
    return console.open( new TestConsoleConfigurer() );
  }
}