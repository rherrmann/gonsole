package com.codeaffine.gonsole.acceptance;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.PageBookView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.internal.GitConsoleFactory;
import com.codeaffine.gonsole.internal.RepositoryProvider;

public class SkeletonPDETest {

  private static final String CONSOLE_VIEW_ID = "org.eclipse.ui.console.ConsoleView";

  private Display display;
  private IWorkbenchPage activePage;
  private RepositoryProvider repositoryProvider;

  @Test
  public void testExecuteSimpleCommand() throws PartInitException {
    TextConsolePage consolePage = openConsolePage();
    StyledText control = ( StyledText )consolePage.getControl();
    String lineDelimiter = control.getLineDelimiter();
    control.append( "status" + lineDelimiter );

    waitForDocumentChange( consolePage.getViewer().getDocument() );

    assertConsoleTextEquals( consolePage, "status", "# On branch master" );
  }

  @Test
  public void testExecuteChangeRepositoryCommand() throws PartInitException {
    TextConsolePage consolePage = openConsolePage();
    enterCommand( consolePage, "cr gonsole-repository-2" );

    waitForDocumentChange( consolePage.getViewer().getDocument() );

    assertConsoleTextEquals( consolePage,
                             "cr gonsole-repository-2",
                             "Current repository is: gonsole-repository-2" );
  }

  @Before
  public void setUp() {
    display = PlatformUI.getWorkbench().getDisplay();
    activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    repositoryProvider = new RepositoryProvider();
    repositoryProvider.deleteRepositories();
    repositoryProvider.ensureRepositories();
    hideIntroPart();
  }

  @After
  public void tearDown() {
    activePage.hideView( activePage.findView( CONSOLE_VIEW_ID ) );
    repositoryProvider.deleteRepositories();
  }

  private static void enterCommand( TextConsolePage consolePage, String commandText ) {
    StyledText control = ( StyledText )consolePage.getControl();
    String lineDelimiter = control.getLineDelimiter();
    control.append( commandText + lineDelimiter );
  }

  private static void assertConsoleTextEquals( TextConsolePage consolePage, String... lines ) {
    StyledText control = ( StyledText )consolePage.getControl();
    String expectedText = "";
    for( int i = 0; i < lines.length; i++ ) {
      expectedText = expectedText + lines[ i ] + control.getLineDelimiter();
    }
    assertEquals( expectedText, control.getText() );
  }

  private TextConsolePage openConsolePage() throws PartInitException {
    new GitConsoleFactory().openConsole();
    IConsole console = ConsolePlugin.getDefault().getConsoleManager().getConsoles()[ 0 ];
    IConsoleView consoleView = ( IConsoleView )activePage.showView( CONSOLE_VIEW_ID );
    consoleView.display( console );
    flushDisplayEventLoop();
    return ( TextConsolePage )( ( PageBookView )consoleView ).getCurrentPage();
  }

  private void hideIntroPart() {
    IWorkbenchPart view = activePage.getActivePart();
    if( "org.eclipse.ui.internal.introview".equals( view.getSite().getId() ) ) {
      IWorkbenchWindow workbenchWindow = view.getSite().getWorkbenchWindow();
      workbenchWindow.getActivePage().hideView( ( IViewPart )view );
      flushDisplayEventLoop();
    }
  }

  private void waitForDocumentChange( IDocument document ) {
    final boolean[] changed = new boolean[ 1 ];
    document.addDocumentListener( new DocumentAdapter() {
      @Override
      public void documentChanged( DocumentEvent event ) {
        changed[ 0 ] = true;
      }
    } );
    long startTime = System.currentTimeMillis();
    while( !changed[ 0 ] && !activePage.getWorkbenchWindow().getShell().isDisposed() ) {
      if( System.currentTimeMillis() - startTime > 10000 ) {
        fail( "Timed out while waiting on console document change" );
      }
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
  }

  private void flushDisplayEventLoop() {
    while( display.readAndDispatch() ) {
    }
  }

  private static class DocumentAdapter implements IDocumentListener {
    @Override
    public void documentAboutToBeChanged( DocumentEvent event ) {
    }

    @Override
    public void documentChanged( DocumentEvent event ) {
    }
  }
}
