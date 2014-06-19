package com.codeaffine.gonsole.internal.gitconsole.preference;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.gonsole.internal.gitconsole.preference.ConsolePreferencePage;

public class ConsolePreferencePagePDETest {

  private ConsolePreferencePage preferencePage;

  @Test
  public void testGetPreferenceStore() {
    IPreferenceStore preferenceStore = preferencePage.getPreferenceStore();

    assertThat( preferenceStore ).isNull();
  }

  @Test
  public void testGetTitle() {
    assertThat( preferencePage.getTitle() ).isNotEmpty();
  }

  @Before
  public void setUp() {
    preferencePage = new ConsolePreferencePage();
  }

  @After
  public void tearDown() {
    preferencePage.dispose();
  }
}
