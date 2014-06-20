package com.codeaffine.gonsole.internal;

import static com.codeaffine.gonsole.internal.activator.IconRegistry.GONSOLE;

import java.nio.charset.Charset;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;

import com.codeaffine.console.core.ColorScheme;
import com.codeaffine.console.core.ConsoleComponentFactory;
import com.codeaffine.console.core.ConsoleDefinition;
import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProviderFactory;
import com.google.common.base.Charsets;

public class GitConsoleDefinition implements ConsoleDefinition {

  private final CompositeRepositoryProvider repositoryProvider;
  private final Display display;

  public GitConsoleDefinition( Display display ) {
    this( display, new CompositeRepositoryProviderFactory().create() );
  }

  public GitConsoleDefinition( Display display, CompositeRepositoryProvider repositoryProvider ) {
    this.repositoryProvider = repositoryProvider;
    this.display = display;
  }

  @Override
  public String getTitle() {
    return "Git Console";
  }

  @Override
  public ImageDescriptor getImage() {
    return new IconRegistry().getDescriptor( GONSOLE );
  }

  @Override
  public String getType() {
    return "com.codeaffine.gonsole.console";
  }

  @Override
  public Charset getEncoding() {
    return Charsets.UTF_8;
  }

  @Override
  public ColorScheme getColorScheme() {
    return new DefaultColorScheme( display );
  }

  @Override
  public ConsoleComponentFactory getConsoleComponentFactory() {
    return new GitConsoleComponentFactory( repositoryProvider );
  }
}