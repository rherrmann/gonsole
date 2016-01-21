package com.codeaffine.gonsole.internal.interpreter;

import static org.eclipse.core.resources.IWorkspace.AVOID_UPDATE;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;

import com.codeaffine.gonsole.util.Repositories;


class SharedResourcesRefresher {

  private final File repositoryLocation;
  private final Collection<IProject> sharedProjects;
  private final IWorkspace workspace;

  SharedResourcesRefresher( File repositoryLocation ) {
    this.repositoryLocation = repositoryLocation;
    this.sharedProjects = new LinkedList<>();
    this.workspace = ResourcesPlugin.getWorkspace();
  }

  void refresh() {
    collectSharedProjects();
    refreshSharedProjects();
  }

  private void collectSharedProjects() {
    IPath workDir = Repositories.getWorkDir( repositoryLocation );
    for( IProject project : getWorkspaceProjects() ) {
      IPath projectLocation = project.getLocation();
      if( projectLocation != null && workDir.isPrefixOf( projectLocation ) ) {
        sharedProjects.add( project );
      }
    }
  }

  private void refreshSharedProjects() {
    IWorkspaceRunnable runnable = new RefreshProjectsRunnable( sharedProjects );
    try {
      workspace.run( runnable, getSchedulingRule(), AVOID_UPDATE, new NullProgressMonitor() );
    } catch( CoreException ce ) {
      handleException( ce );
    }
  }

  private ISchedulingRule getSchedulingRule() {
    ISchedulingRule[] schedulingRules = sharedProjects.stream().toArray( ISchedulingRule[]::new );
    return MultiRule.combine( schedulingRules );
  }

  private IProject[] getWorkspaceProjects() {
    return workspace.getRoot().getProjects( IContainer.INCLUDE_HIDDEN );
  }

  private static void handleException( CoreException exception ) {
    if( !isResourceNotFoundException( exception ) && !isProjectNotOpenException( exception ) ) {
      throw new RuntimeException( exception );
    }
  }

  private static boolean isResourceNotFoundException( CoreException exception ) {
    return exception.getStatus().getCode() == IResourceStatus.RESOURCE_NOT_FOUND;
  }

  private static boolean isProjectNotOpenException( CoreException exception ) {
    return exception.getStatus().getCode() == IResourceStatus.PROJECT_NOT_OPEN;
  }

  private static class RefreshProjectsRunnable implements IWorkspaceRunnable {
    private final Collection<IProject> projects;

    RefreshProjectsRunnable( Collection<IProject> projects ) {
      this.projects = projects;
    }

    @Override
    public void run( IProgressMonitor monitor ) throws CoreException {
      for( IProject project : projects ) {
        project.refreshLocal( IResource.DEPTH_INFINITE, monitor );
      }
    }
  }

}
