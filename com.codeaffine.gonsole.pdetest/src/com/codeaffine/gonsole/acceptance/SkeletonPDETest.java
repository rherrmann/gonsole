package com.codeaffine.gonsole.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

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
  public void testExecuteSimpleCommand() throws Exception {
    TextConsolePage consolePage = openGitConsole();
    StyledText control = ( StyledText )consolePage.getControl();
    String lineDelimiter = control.getLineDelimiter();
    control.append( "status" + lineDelimiter );

    waitForDocumentChange( consolePage.getViewer().getDocument() );

    String expectedText = "status" + lineDelimiter + "# On branch master" + lineDelimiter;
    assertThat( control.getText() ).isEqualTo( expectedText );
  }

  @Before
  public void setUp() {
    display = PlatformUI.getWorkbench().getDisplay();
    activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    repositoryProvider = new RepositoryProvider();
    repositoryProvider.deleteRepository();
    repositoryProvider.ensureRepository();
    hideIntroPart();
  }

  @After
  public void tearDown() {
    activePage.hideView( activePage.findView( CONSOLE_VIEW_ID ) );
    repositoryProvider.deleteRepository();
  }

  private TextConsolePage openGitConsole() throws PartInitException {
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
    while( !changed[ 0 ] && !activePage.getWorkbenchWindow().getShell().isDisposed() ) {
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
