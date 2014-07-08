package com.codeaffine.gonsole.internal;

import static com.codeaffine.gonsole.internal.activator.IconRegistry.GIT_PROPOSAL;

import org.eclipse.jface.resource.ImageDescriptor;

import com.codeaffine.console.core.ConsoleCommandInterpreter;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.ConsolePrompt;
import com.codeaffine.console.core.ContentProposalProvider;
import com.codeaffine.console.core.history.HistoryTracker;
import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.contentassist.ControlCommandContentProposalProvider;
import com.codeaffine.gonsole.internal.contentassist.GitCommandContentProposalProvider;
import com.codeaffine.gonsole.internal.history.PreferenceHistoryStore;
import com.codeaffine.gonsole.internal.interpreter.ControlCommandInterpreter;
import com.codeaffine.gonsole.internal.interpreter.GitCommandInterpreter;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;

class GitConsoleComponentFactory implements ConsoleComponentFactory {

  private final CompositeRepositoryProvider repositoryProvider;
  private final HistoryTracker historyTracker;

  GitConsoleComponentFactory( CompositeRepositoryProvider repositoryProvider ) {
    this.repositoryProvider = repositoryProvider;
    this.historyTracker = createHistoryTracker();
  }

  @Override
  public ConsoleCommandInterpreter[] createCommandInterpreters( ConsoleOutput consoleOutput ) {
    return new ConsoleCommandInterpreter[]{
      new ControlCommandInterpreter( consoleOutput, repositoryProvider ),
      new GitCommandInterpreter( consoleOutput, repositoryProvider )
    };
  }

  @Override
  public ConsolePrompt createConsolePrompt( ConsoleOutput consoleOutput ) {
    return new GitConsolePrompt( repositoryProvider, consoleOutput );
  }

  @Override
  public ContentProposalProvider[] createProposalProviders() {
    return new ContentProposalProvider[] {
      new GitCommandContentProposalProvider(),
      new ControlCommandContentProposalProvider()
    };
  }

  @Override
  public HistoryTracker getHistoryTracker() {
    return historyTracker;
  }

  private static HistoryTracker createHistoryTracker() {
    ImageDescriptor imageDescriptor = new IconRegistry().getDescriptor( GIT_PROPOSAL );
    PreferenceHistoryStore historyStore = new PreferenceHistoryStore();
    return new HistoryTracker( 50, imageDescriptor, historyStore );
  }
}