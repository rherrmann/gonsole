package com.codeaffine.console.core.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.ui.console.TextConsolePage;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.internal.resource.ConsoleIoProvider;

class Processor {

  private final ConsoleComponentFactory componentFactory;
  private final ExecutorService executorService;
  private final ConsoleIoProvider ioProvider;
  private final TextConsolePage page;

  Processor( TextConsolePage page, ConsoleIoProvider ioProvider, ConsoleComponentFactory componentFactory ) {
    this.executorService = Executors.newSingleThreadExecutor();
    this.componentFactory = componentFactory;
    this.ioProvider = ioProvider;
    this.page = page;
  }

  void start() {
    executorService.execute( new ProcessorWorker( page.getViewer(), ioProvider, componentFactory ) );
  }

  void stop() {
    executorService.shutdownNow();
  }
}