package com.codeaffine.gonsole.internal;



import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;

import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

class GitConsolePage implements IPageBookViewPage {

  final TextConsolePage consolePage;
  private final InputObserver inputObserver;

  GitConsolePage( TextConsolePage consolePage,
                  ConsoleIOProvider consoleIOProvider,
                  CompositeRepositoryProvider repositoryProvider )
  {
    this.consolePage = consolePage;
    this.inputObserver = new InputObserver( consoleIOProvider, repositoryProvider );
  }

  @Override
  public void init( IPageSite site ) throws PartInitException {
    consolePage.init( site );
  }

  @Override
  public void createControl( Composite parent ) {
    consolePage.createControl( parent );
    new GitConsoleContentAssist( consolePage.getViewer() ).install();
    inputObserver.start( consolePage.getViewer() );
  }

  @Override
  public void setFocus() {
    consolePage.setFocus();
  }

  @Override
  public void setActionBars( IActionBars actionBars ) {
    consolePage.setActionBars( actionBars );
  }

  @Override
  public Control getControl() {
    return consolePage.getControl();
  }

  @Override
  public IPageSite getSite() {
    return consolePage.getSite();
  }

  @Override
  public void dispose() {
    inputObserver.stop();
    consolePage.dispose();
  }
}