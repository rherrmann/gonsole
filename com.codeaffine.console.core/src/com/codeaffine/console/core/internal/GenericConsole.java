package com.codeaffine.console.core.internal;

import static com.codeaffine.console.core.ConsoleConstants.ENCODING;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;

import com.codeaffine.console.core.ColorScheme;
import com.codeaffine.console.core.Console;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleConfigurer;
import com.codeaffine.console.core.ConsoleOutput;
import com.codeaffine.console.core.internal.resource.ColorDefinition;
import com.codeaffine.console.core.internal.resource.ConsoleIoProvider;

public class GenericConsole extends IOConsole implements Console {

  private final ConsoleConfigurer consoleConfigurer;
  private ConsoleIoProvider consoleIoProvider;
  private Processor processor;
  private ColorDefinition colorDefinition;
  private ColorScheme colorScheme;
  private ConsoleComponentFactory consoleComponentFactory;
  private TextConsolePage consolePage;
  private boolean disposed;

  public GenericConsole( ConsoleConfigurer consoleConfigurer ) {
    super( "", consoleConfigurer.getClass().getName(), null, ENCODING.name(), true );
    this.consoleConfigurer = consoleConfigurer;
  }

  @Override
  public void setTitle( String title ) {
    setName( title );
  }

  @Override
  public void setImageDescriptor( ImageDescriptor imageDescriptor ) {
    super.setImageDescriptor( imageDescriptor );
  }

  @Override
  public void setColorScheme( ColorScheme colorScheme ) {
    this.colorScheme = colorScheme;
    if( colorDefinition != null ) {
      colorDefinition.dispose();
    }
    colorDefinition = new ColorDefinition( colorScheme );
  }

  public ColorScheme getColorScheme() {
    return colorScheme;
  }

  @Override
  public void setConsoleComponentFactory( ConsoleComponentFactory consoleComponentFactory ) {
    this.consoleComponentFactory = consoleComponentFactory;
  }

  @Override
  protected void init() {
    super.init();
    consoleConfigurer.configure( this );
    consoleIoProvider = new ConsoleIoProvider( colorDefinition, new IoStreamProvider( this ) );
    processor = new Processor( consoleIoProvider, consoleComponentFactory );
    processor.start();
  }

  @Override
  public IPageBookViewPage createPage( IConsoleView view ) {
    consolePage = ( TextConsolePage )super.createPage( view );
    return new GenericConsolePage( consolePage, consoleComponentFactory );
  }

  public TextConsolePage getPage() {
    return consolePage;
  }

  @Override
  protected void dispose() {
    if( processor != null ) {
      processor.stop();
    }
    if( colorDefinition != null ) {
      colorDefinition.dispose();
    }
    super.dispose();
    disposed = true;
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  @Override
  public void clearConsole() {
    super.clearConsole();
    ConsoleOutput consoleOutput = Output.create( consoleIoProvider.getPromptStream() );
    consoleComponentFactory.createConsolePrompt( consoleOutput ).writePrompt();
  }
}