package com.codeaffine.gonsole.internal.gitconsole.preference;

import static com.codeaffine.gonsole.internal.gitconsole.preference.WorkspaceScopePreferences.PREF_REPOSITORY_LOCATIONS;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.codeaffine.gonsole.internal.activator.GonsolePlugin;

public class RepositoryPreferencePage
  extends FieldEditorPreferencePage
  implements IWorkbenchPreferencePage
{

  public static final String ID = "com.codeaffine.gonsole.internal.preference.RepositoryPreferencePage";

  private MultiLineFieldEditor repositoryLocationsField;

  public RepositoryPreferencePage() {
    super( "Repository Locations", GRID );
  }

  public Text getRepositoryLocationsControl() {
    return repositoryLocationsField.getTextControl();
  }

  @Override
  public void init( IWorkbench workbench ) {
  }

  @Override
  protected IPreferenceStore doGetPreferenceStore() {
    return GonsolePlugin.getInstance().getPreferenceStore();
  }

  @Override
  protected void createFieldEditors() {
    Composite parent = getFieldEditorParent();
    String label
      = "Repository Locations\n"
      + "(Enter the absolute path to the repository's .git directory,\n"
      + "separate multiple repository locations by newline)";
    repositoryLocationsField = new MultiLineFieldEditor( PREF_REPOSITORY_LOCATIONS, label, parent );
    addField( repositoryLocationsField );
  }
}