package com.codeaffine.gonsole.internal.command;

import static org.eclipse.jface.viewers.StructuredSelection.EMPTY;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class WorkbenchState {
  private final IWorkbenchPart activePart;
  private final ISelection selection;

  public WorkbenchState( ExecutionEvent event ) {
    activePart = HandlerUtil.getActivePart( event );
    selection = HandlerUtil.getCurrentSelection( event );
  }

  public IWorkbenchPart getActivePart() {
    return activePart;
  }

  public IStructuredSelection getSelection() {
    return selection instanceof IStructuredSelection ? ( IStructuredSelection )selection : EMPTY;
  }
}