package com.codeaffine.console.core.internal;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.internal.contentassist.ContentAssist;

class ConsolePage implements IPageBookViewPage {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final TextConsolePage consolePage;

  ConsolePage( TextConsolePage consolePage, ConsoleComponentFactory factory ) {
    this.consolePage = consolePage;
    this.consoleComponentFactory = factory;
  }

  @Override
  public void init( IPageSite site ) throws PartInitException {
    consolePage.init( site );
  }

  @Override
  public void createControl( Composite parent ) {
    consolePage.createControl( parent );
    new ContentAssist( consolePage.getViewer(), consoleComponentFactory ).install();
    new CaretPositionUpdater( consolePage.getViewer() ).install();
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
    consolePage.dispose();
  }
}