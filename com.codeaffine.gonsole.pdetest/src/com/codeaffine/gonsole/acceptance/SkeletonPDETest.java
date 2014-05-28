package com.codeaffine.gonsole.acceptance;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
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

  private RepositoryProvider repositoryProvider;

  @Test
  public void testSkeleton() throws Exception {
    new GitConsoleFactory().openConsole();
    IConsole console = ConsolePlugin.getDefault().getConsoleManager().getConsoles()[ 0 ];

    IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    IConsoleView consoleView = ( IConsoleView )activePage.showView( "org.eclipse.ui.console.ConsoleView" );
    consoleView.display( console );
    Display display = Display.getDefault();
    while( display.readAndDispatch() ) {
    }

    TextConsolePage consolePage = ( TextConsolePage )( ( PageBookView )consoleView ).getCurrentPage();
    StyledText control = ( StyledText )consolePage.getControl();
    String lineDelimiter = control.getLineDelimiter();
    control.append( "status" + lineDelimiter );

    final boolean[] done = new boolean[ 1 ];
    consolePage.getViewer().getDocument().addDocumentListener( new IDocumentListener() {
      @Override
      public void documentAboutToBeChanged( DocumentEvent event ) {
      }

      @Override
      public void documentChanged( DocumentEvent event ) {
        done[ 0 ] = true;
      }
    } );

    waitInEventLoop( done );

    String expectedText = "status" + lineDelimiter + "# On branch master" + lineDelimiter;
    assertEquals( expectedText, control.getText() );
  }

  private static void waitInEventLoop( boolean[] done ) {
    Display display = Display.getDefault();
    while( !done[ 0 ] && !PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
  }

  @Before
  public void setUp() {
    repositoryProvider = new RepositoryProvider();
    repositoryProvider.deleteRepository();
    repositoryProvider.ensureRepository();
    hideIntroPart();
  }

  @After
  public void tearDown() {
    IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    activePage.hideView( activePage.findView( "org.eclipse.ui.console.ConsoleView" ) );
    repositoryProvider.deleteRepository();
  }

  public static void hideIntroPart() {
    IWorkbenchPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
    if( "org.eclipse.ui.internal.introview".equals( view.getSite().getId() ) ) {
      IWorkbenchWindow workbenchWindow = view.getSite().getWorkbenchWindow();
      workbenchWindow.getActivePage().hideView( ( IViewPart )view );
      while( Display.getDefault().readAndDispatch() ) {
      }
    }
  }

}
