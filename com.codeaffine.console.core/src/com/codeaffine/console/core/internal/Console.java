package com.codeaffine.console.core.internal;

import static com.codeaffine.console.core.ConsoleConstants.ENCODING;

import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;

import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleDefinition;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.internal.resource.ColorDefinition;
import com.codeaffine.console.core.internal.resource.ConsoleIoProvider;

public class Console extends IOConsole {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final ConsoleIoProvider consoleIoProvider;
  private final ColorDefinition colorDefinition;
  private final Processor processor;

  private TextConsolePage consolePage;

  public Console( ConsoleDefinition definition ) {
    super( definition.getTitle(), definition.getClass().getName(), definition.getImage(), ENCODING.name(), true );
    this.colorDefinition = new ColorDefinition( definition.getColorScheme() );
    this.consoleIoProvider = createConsoleIoProvider();
    this.consoleComponentFactory = definition.getConsoleComponentFactory();
    this.processor = new Processor( consoleIoProvider, consoleComponentFactory );
  }

  @Override
  protected void init() {
    super.init();
    processor.start();
  }

  @Override
  public IPageBookViewPage createPage( IConsoleView view ) {
    consolePage = ( TextConsolePage )super.createPage( view );
    return new ConsolePage( consolePage, consoleComponentFactory );
  }

  public TextConsolePage getPage() {
    return consolePage;
  }

  @Override
  protected void dispose() {
    processor.stop();
    colorDefinition.dispose();
    super.dispose();
  }

  @Override
  public void clearConsole() {
    super.clearConsole();
    ConsoleOutput consoleOutput = Output.create( consoleIoProvider.getPromptStream() );
    consoleComponentFactory.createConsolePrompt( consoleOutput ).writePrompt();
  }

  private ConsoleIoProvider createConsoleIoProvider() {
    IoStreamProvider ioStreamProvider = new IoStreamProvider( this );
    return new ConsoleIoProvider( colorDefinition, ioStreamProvider );
  }
}