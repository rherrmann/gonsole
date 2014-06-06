package com.codeaffine.gonsole.acceptance;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
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
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.PageBookView;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.codeaffine.gonsole.RepositoryProvider;
import com.codeaffine.gonsole.internal.GitConsole;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

public class ConsoleSkeletonPDETest {

  private static final String CONSOLE_VIEW_ID = "org.eclipse.ui.console.ConsoleView";

  @Rule
  public final TemporaryFolder tempFolder = new TemporaryFolder();

  private Display display;
  private IWorkbenchPage activePage;
  private File[] repositoryLocations;

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
    enterCommand( consolePage, "cr repo-2" );

    waitForDocumentChange( consolePage.getViewer().getDocument() );

    assertConsoleTextEquals( consolePage, "cr repo-2", "Current repository is: repo-2" );
  }

  @Before
  public void setUp() throws GitAPIException {
    display = PlatformUI.getWorkbench().getDisplay();
    activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    repositoryLocations = new File[] {
      createRepository( new File( tempFolder.getRoot(), "repo-1" ) ),
      createRepository( new File( tempFolder.getRoot(), "repo-2" ) )
    };
    hideIntroPart();
  }

  @After
  public void tearDown() {
    activePage.hideView( activePage.findView( CONSOLE_VIEW_ID ) );
    removeGitConsoles();
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

  private static File createRepository( File file ) throws GitAPIException {
    Git git = Git.init().setDirectory( file ).call();
    File result = git.getRepository().getDirectory();
    git.getRepository().close();
    return result;
  }

  private CompositeRepositoryProvider createCompositeRepositoryProvider() {
    RepositoryProvider repositoryProvider = mock( RepositoryProvider.class );
    when( repositoryProvider.getRepositoryLocations() ).thenReturn( repositoryLocations );
    CompositeRepositoryProvider result = new CompositeRepositoryProvider( repositoryProvider );
    result.setCurrentRepositoryLocation( repositoryProvider.getRepositoryLocations()[ 0 ] );
    return result;
  }

  private TextConsolePage openConsolePage() throws PartInitException {
    CompositeRepositoryProvider repositoryProvider2 = createCompositeRepositoryProvider();
    IConsole console = new GitConsole( repositoryProvider2 );
    ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[] { console } );
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

  private static void removeGitConsoles() {
    IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    IConsole[] consoles = consoleManager.getConsoles();
    for( IConsole console : consoles ) {
      if( console instanceof GitConsole ) {
        consoleManager.removeConsoles( new IConsole[] { console } );
      }
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
