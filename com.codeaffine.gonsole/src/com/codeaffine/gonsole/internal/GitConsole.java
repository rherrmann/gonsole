package com.codeaffine.gonsole.internal;

import static com.codeaffine.gonsole.internal.activator.IconRegistry.GONSOLE;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;

import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.google.common.base.Charsets;

public class GitConsole extends IOConsole {

  private static final String TYPE = "com.codeaffine.gonsole.console";
  private static final ImageDescriptor IMAGE = new IconRegistry().getDescriptor( GONSOLE );
  private static final String ENCODING = Charsets.UTF_8.name();

  private final CompositeRepositoryProvider repositoryProvider;
  private volatile TextConsolePage consolePage;
  private DefaultConsoleIOProvider consoleIOProvider;

  public GitConsole( CompositeRepositoryProvider repositoryProvider ) {
    super( "Git Console", TYPE, IMAGE, ENCODING, true );
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  protected void init() {
    super.init();
    consoleIOProvider = new DefaultConsoleIOProvider( Display.getCurrent(), this );
  }

  @Override
  public IPageBookViewPage createPage( IConsoleView view ) {
    consolePage = ( TextConsolePage )super.createPage( view );
    return new GitConsolePage( consolePage, consoleIOProvider, repositoryProvider );
  }

  public TextConsolePage getPage() {
    return consolePage;
  }
}