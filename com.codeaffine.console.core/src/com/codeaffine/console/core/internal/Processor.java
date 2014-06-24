package com.codeaffine.console.core.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.internal.resource.ConsoleIoProvider;

class Processor {

  private final ConsoleComponentFactory componentFactory;
  private final ExecutorService executorService;
  private final ConsoleIoProvider ioProvider;

  Processor( ConsoleIoProvider ioProvider, ConsoleComponentFactory componentFactory ) {
    this.executorService = Executors.newSingleThreadExecutor();
    this.componentFactory = componentFactory;
    this.ioProvider = ioProvider;
  }

  void start() {
    executorService.execute( new ProcessorWorker( ioProvider, componentFactory ) );
  }

  void stop() {
    executorService.shutdownNow();
  }
}