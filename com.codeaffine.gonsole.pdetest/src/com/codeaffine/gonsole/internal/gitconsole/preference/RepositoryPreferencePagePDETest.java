package com.codeaffine.gonsole.internal.gitconsole.preference;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.internal.activator.GonsolePlugin;
import com.codeaffine.gonsole.internal.gitconsole.preference.RepositoryPreferencePage;

public class RepositoryPreferencePagePDETest {

  private RepositoryPreferencePage preferencePage;

  @Test
  public void testGetPreferenceStore() {
    IPreferenceStore preferenceStore = preferencePage.getPreferenceStore();

    assertThat( preferenceStore ).isEqualTo( GonsolePlugin.getInstance().getPreferenceStore() );
  }

  @Test
  public void testGetTitle() {
    assertThat( preferencePage.getTitle() ).isNotEmpty();
  }

  @Before
  public void setUp() {
    preferencePage = new RepositoryPreferencePage();
  }

  @After
  public void tearDown() {
    preferencePage.dispose();
  }
}
