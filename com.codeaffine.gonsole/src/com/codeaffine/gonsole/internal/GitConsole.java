package com.codeaffine.gonsole.internal;

import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;

import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.google.common.base.Charsets;

public class GitConsole extends IOConsole implements ConsoleIOProvider {

  private static final String TYPE = "com.codeaffine.gonsole.console";
  private static final String ENCODING = Charsets.UTF_8.name();

  private final CompositeRepositoryProvider repositoryProvider;
  private volatile TextConsolePage consolePage;

  public GitConsole( CompositeRepositoryProvider repositoryProvider ) {
    super( "Git Console", TYPE, new IconRegistry().getDescriptor( IconRegistry.GONSOLE ), ENCODING, true );
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public IPageBookViewPage createPage( IConsoleView view ) {
    consolePage = ( TextConsolePage )super.createPage( view );
    return new GitConsolePage( consolePage, this, repositoryProvider );
  }

  public TextConsolePage getPage() {
    return consolePage;
  }
}