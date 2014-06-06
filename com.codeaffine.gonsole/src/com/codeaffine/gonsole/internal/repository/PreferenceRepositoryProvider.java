package com.codeaffine.gonsole.internal.repository;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;

import java.io.File;
import java.util.regex.Pattern;

import com.codeaffine.gonsole.RepositoryProvider;
import com.codeaffine.gonsole.internal.preference.WorkspaceScopePreferences;
import com.google.common.base.Function;
import com.google.common.base.Splitter;


public class PreferenceRepositoryProvider implements RepositoryProvider {

  private static final Pattern NEW_LINE_PATTERN = Pattern.compile( "\r?\n" );

  private final WorkspaceScopePreferences preferences;

  public PreferenceRepositoryProvider() {
    this( new WorkspaceScopePreferences() );
  }

  public PreferenceRepositoryProvider( WorkspaceScopePreferences preferences ) {
    this.preferences = preferences;
  }

  @Override
  public File[] getRepositoryLocations() {
    Iterable<String> locationNames = getLocationNames();
    Iterable<File> locationFiles = transform( locationNames, new StringToFileTransform() );
    return toArray( locationFiles, File.class );
  }

  private Iterable<String> getLocationNames() {
    return Splitter.on( NEW_LINE_PATTERN )
      .trimResults()
      .omitEmptyStrings()
      .split( preferences.getRepositoryLocations() );
  }

  private static class StringToFileTransform implements Function<String, File> {
    @Override
    public File apply( String input ) {
      return new File( input );
    }
  }

}
