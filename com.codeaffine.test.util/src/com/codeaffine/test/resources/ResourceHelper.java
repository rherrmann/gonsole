package com.codeaffine.test.resources;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;


public class ResourceHelper {

  public static void delete( IResource resource ) throws CoreException {
    try {
      resource.delete( true, new NullProgressMonitor() );
    } catch( CoreException ce ) {
      System.gc();
      System.gc();
      resource.delete( true, new NullProgressMonitor() );
    }
  }

  public static void delete( IProject project )
    throws CoreException
  {
    try {
      project.delete( true, true, new NullProgressMonitor() );
    } catch( CoreException ce ) {
      System.gc();
      System.gc();
      project.delete( true, true, new NullProgressMonitor() );
    }
  }

  private ResourceHelper() {
    // prevent instantiation
  }
}
