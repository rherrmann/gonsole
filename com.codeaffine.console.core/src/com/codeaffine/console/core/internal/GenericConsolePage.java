package com.codeaffine.console.core.internal;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.internal.contentassist.ContentAssist;

class GenericConsolePage implements IPageBookViewPage, IAdaptable {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final TextConsolePage consolePage;

  GenericConsolePage( TextConsolePage consolePage, ConsoleComponentFactory factory ) {
    this.consolePage = consolePage;
    this.consoleComponentFactory = factory;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Object getAdapter( Class adapter ) {
    return consolePage.getAdapter( adapter );
  }

  @Override
  public void init( IPageSite site ) throws PartInitException {
    consolePage.init( site );
  }

  @Override
  public void createControl( Composite parent ) {
    consolePage.createControl( parent );
    ConsoleEditor editor = new ConsoleEditor( consolePage.getViewer() );
    new ContentAssist( editor, consoleComponentFactory ).install();
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