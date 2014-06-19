package com.codeaffine.gonsole.internal;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;

import com.codeaffine.gonsole.ConsoleComponentFactory;
import com.codeaffine.gonsole.internal.resource.ConsoleIoProvider;

class ConsolePage implements IPageBookViewPage {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final TextConsolePage consolePage;
  private final InputObserver inputObserver;

  ConsolePage( TextConsolePage consolePage, ConsoleIoProvider ioProvider, ConsoleComponentFactory factory  ) {
    this.inputObserver = new InputObserver( ioProvider, factory );
    this.consoleComponentFactory = factory;
    this.consolePage = consolePage;
  }

  @Override
  public void init( IPageSite site ) throws PartInitException {
    consolePage.init( site );
  }

  @Override
  public void createControl( Composite parent ) {
    consolePage.createControl( parent );
    new GitConsoleContentAssist( consolePage.getViewer(), consoleComponentFactory ).install();
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