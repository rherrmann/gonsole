package com.codeaffine.gonsole.internal.preference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.codeaffine.gonsole.internal.preference.MultiLineFieldEditor;
import com.codeaffine.test.util.swt.DisplayHelper;

public class MultiLineFieldEditorPDETest {

  private static final String PREF_NAME = "preference-name";

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private PreferenceStore preferenceStore;
  private MultiLineFieldEditor fieldEditor;

  @Test
  public void testGetTextControl() {
    Text textControl = fieldEditor.getTextControl();

    assertThat( textControl.getStyle() & SWT.MULTI ).isNotZero();
    assertThat( textControl.getStyle() & SWT.BORDER ).isNotZero();
  }

  @Test
  public void testInitialPresentsDefaultValue() {
    assertThat( fieldEditor.presentsDefaultValue() ).isFalse();
  }

  @Test
  public void testLoad() {
    preferenceStore.setValue( PREF_NAME, "value" );

    fieldEditor.load();

    assertThat( fieldEditor.getTextControl().getText() ).isEqualTo( "value" );
  }

  @Test
  public void testLoadDefault() {
    preferenceStore.setDefault( PREF_NAME, "default-value" );

    fieldEditor.loadDefault();

    assertThat( fieldEditor.getTextControl().getText() ).isEqualTo( "default-value" );
  }

  @Test
  public void testStore() {
    fieldEditor.getTextControl().setText( "value" );
    fieldEditor.getTextControl().notifyListeners( SWT.Modify, null );

    fieldEditor.store();

    assertThat( preferenceStore.getString( PREF_NAME ) ).isEqualTo( "value" );
  }

  @Test
  public void testChangeText() {
    IPropertyChangeListener listener = mock( IPropertyChangeListener.class );
    fieldEditor.setPropertyChangeListener( listener );

    fieldEditor.getTextControl().setText( "value" );
    fieldEditor.getTextControl().notifyListeners( SWT.Modify, null );

    ArgumentCaptor<PropertyChangeEvent> captor = forClass( PropertyChangeEvent.class );
    verify( listener ).propertyChange( captor.capture() );
    assertThat( captor.getValue().getProperty() ).isEqualTo( FieldEditor.VALUE );
    assertThat( captor.getValue().getOldValue() ).isEqualTo( "" );
    assertThat( captor.getValue().getNewValue() ).isEqualTo( "value" );
  }

  @Test
  public void testDispose() {
    fieldEditor.getTextControl().getShell().dispose();

    assertThat( fieldEditor.getTextControl() ).isNull();
  }

  @Before
  public void setUp() {
    preferenceStore = new PreferenceStore();
    fieldEditor = new MultiLineFieldEditor( PREF_NAME, "label", displayHelper.createShell() );
    fieldEditor.setPreferenceStore( preferenceStore );
  }
}
