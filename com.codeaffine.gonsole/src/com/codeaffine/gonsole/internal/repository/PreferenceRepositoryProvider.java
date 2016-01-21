package com.codeaffine.gonsole.internal.repository;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.codeaffine.gonsole.RepositoryProvider;
import com.codeaffine.gonsole.internal.preference.WorkspaceScopePreferences;


public class PreferenceRepositoryProvider implements RepositoryProvider {

  private static final String NEW_LINE_PATTERN = "\r?\n";

  private final WorkspaceScopePreferences preferences;

  public PreferenceRepositoryProvider() {
    this( new WorkspaceScopePreferences() );
  }

  public PreferenceRepositoryProvider( WorkspaceScopePreferences preferences ) {
    this.preferences = preferences;
  }

  @Override
  public File[] getRepositoryLocations() {
    return getLocationNames().stream().map( File::new ).toArray( File[]::new );
  }

  private Collection<String> getLocationNames() {
    String[] lines = preferences.getRepositoryLocations().split( NEW_LINE_PATTERN );
    return Stream.of( lines )
      .map( String::trim )
      .filter( line -> !line.isEmpty() )
      .collect( Collectors.toList() );
  }
}
