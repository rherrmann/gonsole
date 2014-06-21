package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.TextInputBot.offset;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.TextConsoleViewer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.internal.Console;
import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.console.core.pdetest.console.TestConsoleDefinition;

public class ContentAssistActionPDETest {

  @Rule public final ConsoleBot consoleBot = new ConsoleBot();

  private ContentAssistant contentAssistant;
  private ContentAssistAction action;
  private TextConsoleViewer viewer;
  private StyledText textWidget;
  private IDocument document;

  @Test
  public void testCreate() throws BadLocationException {
    action.run();

    assertThat( getPartitionType( offset( 0 ) ) ).isEqualTo( PartitionType.INPUT );
    verify( contentAssistant ).showPossibleCompletions();
  }

  @Test
  public void testCreateWithCaretBeforePrompt() throws BadLocationException {
    equipTextWithCaretOffset( 0 );

    action.run();

    assertThat( getPartitionType( offset( 0 ) ) ).isEqualTo( PartitionType.OUTPUT );
    verify( contentAssistant ).showPossibleCompletions();
  }

  @Test
  public void testCreateWithCaretAtBadLocation() throws BadLocationException {
    equipTextWithCaretOffset( 100 );

    action.run();

    assertThat( getPartitionType( offset( 0 ) ) ).isEqualTo( PartitionType.OUTPUT );
    verify( contentAssistant ).showPossibleCompletions();
  }

  @Test
  public void testCreateIfPartitionerHasWrongType() throws BadLocationException {
    equipDocumentWithSimplePartitioner();

    action.run();

    assertThat( getPartitionType( offset( 0 ) ) ).isEqualTo( PartitionType.OUTPUT );
    verify( contentAssistant ).showPossibleCompletions();
  }

  @Test
  public void testCreateTwice() throws BadLocationException {
    action.run();
    ITypedRegion first = document.getPartition( offset( 0 ) );
    action.run();
    ITypedRegion second= document.getPartition( offset( 0 ) );

    assertThat( getPartitionType( offset( 0 ) ) ).isEqualTo( PartitionType.INPUT );
    assertThat( first ).isSameAs( second );
    verify( contentAssistant, times( 2 ) ).showPossibleCompletions();
  }

  @Before
  public void setUp() {
    Console console = consoleBot.open( new TestConsoleDefinition() );
    viewer = spy( console.getPage().getViewer() );
    textWidget = spyTextWidget( viewer );
    document = spyDocument( viewer );
    contentAssistant = mock( ContentAssistant.class );
    action = new ContentAssistAction( contentAssistant, new DocumentHolder( viewer ) );
  }

  private static StyledText spyTextWidget( TextConsoleViewer viewerSpy ) {
    StyledText result = spy( viewerSpy.getTextWidget() );
    when( viewerSpy.getTextWidget() ).thenReturn( result );
    return result;
  }

  private static IDocument spyDocument( TextConsoleViewer viewer ) {
    IDocument result = spy( viewer.getDocument() );
    when( viewer.getDocument() ).thenReturn( result );
    return result;
  }

  private void equipTextWithCaretOffset( int carretOffset ) {
    doReturn( carretOffset ).when( textWidget ).getCaretOffset();
  }

  private void equipDocumentWithSimplePartitioner() {
    IDocumentPartitioner partitioner = mock( IDocumentPartitioner.class );
    doReturn( partitioner ).when( document ).getDocumentPartitioner();
  }

  private String getPartitionType( int offset ) throws BadLocationException {
    return document.getPartition( offset ).getType();
  }
}