package com.codeaffine.gonsole.internal.preference;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class ConsolePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

  public static final String ID
    = "com.codeaffine.gonsole.internal.preference.ConsolePreferencePage";

  public ConsolePreferencePage() {
    super( "Git Console" );
  }

  @Override
  public void init( IWorkbench workbench ) {
  }

  @Override
  protected Control createContents( Composite parent ) {
    Label result = new Label( parent, SWT.NONE );
    result.setText( "Settings related to the Git Command Console" );
    result.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    return result;
  }
}
