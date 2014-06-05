package com.codeaffine.gonsole;

import java.io.File;


/**
 * Implementations of <code>RepositoryProvider</code> know about the location of Git repositories.
 * They are queried by the Git Console when a list of all <em>known</em> repositories is needed.
 *
 * <p>
 * The extension point used to contribute a repository provider is
 * <code>com.codeaffine.gonsole.repositoryProvider</code>.
 * </p>
 */
public interface RepositoryProvider {

  /**
   * This method must return the repositories known by this repository provider. If no
   * repositories are available, an empty array must be returned.
   * <p>
   * A repository is described through the canonical path to its <code>.git</code> directory.
   * </p>
   *
   * @return an array of repository locations
   */
  File[] getRepositoryLocations();
}
