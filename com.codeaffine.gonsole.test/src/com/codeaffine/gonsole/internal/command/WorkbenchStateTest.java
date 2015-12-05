package com.codeaffine.gonsole.internal.command;

import static java.util.Collections.EMPTY_MAP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.ui.ISources.ACTIVE_CURRENT_SELECTION_NAME;
import static org.eclipse.ui.ISources.ACTIVE_PART_NAME;
import static org.mockito.Mockito.mock;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.test.util.workbench.PartHelper;

public class WorkbenchStateTest {

  private PartHelper partHelper;

  @Before
  public void setUp() {
    partHelper = new PartHelper();
  }

  @Test
  public void testGetActivePart() {
    IWorkbenchPart activePart = partHelper.getActivePage().getActivePart();
    WorkbenchState workbenchState = createWorkbenchState( activePart, new StructuredSelection() );

    IWorkbenchPart returnedActivePart = workbenchState.getActivePart();

    assertThat( returnedActivePart ).isEqualTo( activePart );
  }

  @Test
  public void testGetActivePartWhenNoPartActivated() {
    WorkbenchState workbenchState = createWorkbenchState( null, new StructuredSelection() );

    IWorkbenchPart returnedActivePart = workbenchState.getActivePart();

    assertThat( returnedActivePart ).isNull();
  }

  @Test
  public void testGetSelection() {
    ISelection selection = new StructuredSelection();
    WorkbenchState workbenchState = createWorkbenchState( null, selection );

    IStructuredSelection returnedSelection = workbenchState.getSelection();

    assertThat( returnedSelection ).isEqualTo( selection );
  }

  @Test
  public void testGetSelectionWhenNothingSelected() {
    WorkbenchState workbenchState = createWorkbenchState( null, null );

    IStructuredSelection returnedSelection = workbenchState.getSelection();

    assertThat( returnedSelection.isEmpty() ).isTrue();
  }

  @Test
  public void testGetSelectionWhenNoStructuredSelected() {
    ISelection selection = mock( ISelection.class );
    WorkbenchState workbenchState = createWorkbenchState( null, selection );

    IStructuredSelection returnedSelection = workbenchState.getSelection();

    assertThat( returnedSelection.isEmpty() ).isTrue();
  }

  private static WorkbenchState createWorkbenchState( IWorkbenchPart activePart, ISelection selection ) {
    ExecutionEvent event = createExecutionEvent( activePart, selection );
    return new WorkbenchState( event );
  }

  private static ExecutionEvent createExecutionEvent( IWorkbenchPart activePart, ISelection selection ) {
    EvaluationContext context = new EvaluationContext( null, new Object() );
    if( activePart != null ) {
      context.addVariable( ACTIVE_PART_NAME, activePart );
    }
    if( selection != null ) {
      context.addVariable( ACTIVE_CURRENT_SELECTION_NAME, selection );
    }
    return new ExecutionEvent( null, EMPTY_MAP, null, context );
  }

}
