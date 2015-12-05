package com.codeaffine.gonsole.internal.command;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.ide.ResourceUtil;


class RepositoryLocationComputer {

  private final IStructuredSelection selection;
  private final IWorkbenchPart activePart;

  RepositoryLocationComputer( WorkbenchState workbenchState ) {
    this.selection = workbenchState.getSelection();
    this.activePart = workbenchState.getActivePart();
  }

  File compute() {
    File result = null;
    IResource[] selectedResources = getSelectedResources();
    for( int i = 0; result == null && i < selectedResources.length; i++ ) {
      result = getRepositoryLocation( selectedResources[ i ] );
    }
    return result;
  }

  private static File getRepositoryLocation( IResource resource ) {
    File result = null;
    IPath resourceLocation = resource.getLocation();
    if( resourceLocation != null ) {
      File workDirCandidate = toCanonicalFile( resourceLocation.toFile() );
      while( result == null && workDirCandidate != null ) {
        result = getRepositoryDirectory( workDirCandidate );
        workDirCandidate = workDirCandidate.getParentFile();
      }
    }
    return result;
  }

  private static File getRepositoryDirectory( File directory ) {
    File result = null;
    FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
    repositoryBuilder.setMustExist( true );
    repositoryBuilder.setWorkTree( directory );
    try {
      Repository repository = repositoryBuilder.build();
      result = repository.getDirectory();
      repository.close();
    } catch( Exception ignore ) {
    }
    return result;
  }

  private IResource[] getSelectedResources() {
    Set<IResource> result = new HashSet<IResource>();
    IEditorInput editorInput = getActiveEditorInput();
    if( editorInput != null ) {
      IResource resource = ResourceUtil.getResource( editorInput );
      if( resource != null ) {
        result.add( resource );
      }
    } else {
      for( Object element : selection.toList() ) {
        IResource resource = ResourceUtil.getResource( element );
        if( resource != null ) {
          result.add( resource );
        }
      }
    }
    return result.toArray( new IResource[ result.size() ] );
  }

  private IEditorInput getActiveEditorInput() {
    IEditorInput result = null;
    if( activePart instanceof IEditorPart ) {
      IEditorPart editorPart = ( IEditorPart )activePart;
      result = editorPart.getEditorInput();
    }
    return result;
  }

  private static File toCanonicalFile( File file ) {
    try {
      return file.getCanonicalFile();
    } catch( IOException ioe ) {
      throw new RuntimeException( ioe );
    }
  }

}
