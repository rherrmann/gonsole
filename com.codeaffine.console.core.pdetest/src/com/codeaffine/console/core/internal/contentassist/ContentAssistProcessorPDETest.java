package com.codeaffine.console.core.internal.contentassist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.SWT;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.internal.ConsoleEditor;
import com.codeaffine.console.core.pdetest.console.TestConsoleCommandInterpreter;
import com.codeaffine.console.core.pdetest.console.TestConsoleComponentFactory;
import com.codeaffine.test.util.ConditionalIgnoreRule;
import com.codeaffine.test.util.ConditionalIgnoreRule.ConditionalIgnore;
import com.codeaffine.test.util.GtkPlatform;
import com.codeaffine.test.util.swt.DisplayHelper;
import com.codeaffine.test.util.swt.SWTEventHelper;

public class ContentAssistProcessorPDETest {

  private static final String COMMAND = TestConsoleCommandInterpreter.COMMANDS.iterator().next();

  @Rule
  public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ContentAssistProcessor processor;
  private TextViewer viewer;

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testComputeCompletionWithOffsetOnLineEnd() {
    prepareContentAssistOn( COMMAND );

    ICompletionProposal[] actual = processor.computeCompletionProposals( viewer, COMMAND.length() );

    assertThat( actual ).hasSize( 1 );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testComputeCompletionProposalsWithOffsetOnLineStart() {
    prepareContentAssistOn( COMMAND );

    ICompletionProposal[] actual = processor.computeCompletionProposals( viewer, 0 );

    assertThat( actual ).hasSize( 3 );
  }

  @Test
  @ConditionalIgnore(condition=GtkPlatform.class)
  public void testComputeCompletionProposalsWithNonMatchingCommand() {
    prepareContentAssistOn( "unknown" );

    ICompletionProposal[] actual = processor.computeCompletionProposals( viewer, "unknown".length() );

    assertThat( actual ).isEmpty();
  }

  @Test
  public void testDispose() {
    ProposalCalculator calculator = mock( ProposalCalculator.class );
    ContentAssistProcessor contentAssistProcessor = new ContentAssistProcessor( calculator, null );

    contentAssistProcessor.dispose();

    verify( calculator ).dispose();
  }

  @Before
  public void setUp() {
    ConsoleComponentFactory factory = new TestConsoleComponentFactory();
    viewer = new TextViewer( displayHelper.createShell(), SWT.NONE );
    viewer.setDocument( new Document() );
    ConsoleEditor consoleEditor = new ConsoleEditor( viewer );
    consoleEditor.addAction( ctrlSpace(), mock( IAction.class ) );
    processor = new ContentAssistProcessor( factory, consoleEditor );
  }

  private static KeyStroke ctrlSpace() {
    return KeyStroke.getInstance( SWT.MOD1, SWT.SPACE );
  }

  private void prepareContentAssistOn( String text ) {
    viewer.getDocument().set( text );
    simulateContentAssistKeySequence();
  }

  private void simulateContentAssistKeySequence() {
    SWTEventHelper.trigger( SWT.KeyDown )
      .withKeyCode( ' ' )
      .withStateMask( SWT.CTRL )
      .on( viewer.getControl() );
  }
}