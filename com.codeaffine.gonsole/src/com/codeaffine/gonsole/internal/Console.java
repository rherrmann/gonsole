package com.codeaffine.gonsole.internal;

import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;

import com.codeaffine.gonsole.ConsoleComponentFactory;
import com.codeaffine.gonsole.ConsoleDefinition;
import com.codeaffine.gonsole.ConsoleOutput;
import com.codeaffine.gonsole.internal.resource.ColorDefinition;
import com.codeaffine.gonsole.internal.resource.ConsoleIoProvider;

public class Console extends IOConsole {

  private final ConsoleComponentFactory consoleComponentFactory;
  private final ConsoleIoProvider consoleIoProvider;
  private final ColorDefinition colorDefinition;

  private volatile TextConsolePage consolePage;

  public Console( ConsoleDefinition definition ) {
    super( definition.getTitle(), definition.getType(), definition.getImage(), definition.getEncoding().name(), true );
    this.colorDefinition = new ColorDefinition( definition.getColorScheme() );
    this.consoleIoProvider = createConsoleIoProvider( definition, colorDefinition );
    this.consoleComponentFactory = definition.getConsoleComponentFactory();
  }

  @Override
  protected void init() {
    super.init();
  }

  @Override
  public IPageBookViewPage createPage( IConsoleView view ) {
    consolePage = ( TextConsolePage )super.createPage( view );
    return new GitConsolePage( consolePage, consoleIoProvider, consoleComponentFactory );
  }

  public TextConsolePage getPage() {
    return consolePage;
  }

  @Override
  protected void dispose() {
    super.dispose();
    colorDefinition.dispose();
  }

  @Override
  public void clearConsole() {
    super.clearConsole();
    ConsoleOutput consoleOutput = Output.create( consoleIoProvider.getPromptStream(), consoleIoProvider );
    consoleComponentFactory.createConsolePrompt( consoleOutput ).writePrompt();
  }

  private ConsoleIoProvider createConsoleIoProvider( ConsoleDefinition definition, ColorDefinition colorDefinition ) {
    IoStreamProvider ioStreamProvider = new IoStreamProvider( this );
    return new ConsoleIoProvider( colorDefinition, ioStreamProvider, definition.getEncoding() );
  }
}