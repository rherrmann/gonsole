package com.codeaffine.gonsole.internal.preference;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class MultiLineFieldEditor extends FieldEditor {

  private Text textControl;
  private String oldValue;

  public MultiLineFieldEditor( String name, String labelText, Composite parent ) {
    super( name, labelText, parent );
  }

  @Override
  public int getNumberOfControls() {
    return 1;
  }

  public Text getTextControl() {
    return textControl;
  }

  @Override
  protected void adjustForNumColumns( int numColumns ) {
    GridData labelGridData = ( GridData )getLabelControl().getLayoutData();
    labelGridData.horizontalSpan = numColumns - 1;
    GridData textGridData = ( GridData )textControl.getLayoutData();
    textGridData.horizontalSpan = numColumns - 1;
  }

  @Override
  protected void doFillIntoGrid( Composite parent, int numColumns ) {
    Label labelControl = getLabelControl( parent );
    GridData labelGridData = new GridData( SWT.FILL, SWT.TOP, true, false, numColumns, 1 );
    labelControl.setLayoutData( labelGridData );
    textControl = getTextControl( parent );
    GridData textGridData = new GridData( SWT.FILL, SWT.FILL, true, true, numColumns, 1 );
    textControl.setLayoutData( textGridData );
  }

  @Override
  protected void doLoad() {
    if( textControl != null ) {
      String value = getPreferenceStore().getString( getPreferenceName() );
      textControl.setText( value );
      oldValue = value;
    }
  }

  @Override
  protected void doLoadDefault() {
    if( textControl != null ) {
      String value = getPreferenceStore().getDefaultString( getPreferenceName() );
      textControl.setText( value );
    }
    valueChanged();
  }

  @Override
  protected void doStore() {
    getPreferenceStore().setValue( getPreferenceName(), textControl.getText() );
  }

  private Text getTextControl( Composite parent ) {
    if( textControl == null ) {
      textControl = new Text( parent, SWT.MULTI | SWT.BORDER );
      textControl.setFont( parent.getFont() );
      textControl.addModifyListener( new ModifyListener() {
        @Override
        public void modifyText( ModifyEvent event ) {
          valueChanged();
        }
      } );
      textControl.addDisposeListener( new DisposeListener() {
        @Override
        public void widgetDisposed( DisposeEvent event ) {
          textControl = null;
        }
      } );
    } else {
      checkParent(textControl, parent);
    }
    return textControl;
  }

  private void valueChanged() {
    setPresentsDefaultValue( false );
    String newValue = textControl.getText();
    if( !newValue.equals( oldValue ) ) {
      fireValueChanged( VALUE, oldValue, newValue );
      oldValue = newValue;
    }
  }
}
