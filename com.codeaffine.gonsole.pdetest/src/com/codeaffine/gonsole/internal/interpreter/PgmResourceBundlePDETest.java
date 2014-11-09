package com.codeaffine.gonsole.internal.interpreter;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jgit.errors.TranslationBundleLoadingException;
import org.eclipse.jgit.pgm.internal.CLIText;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.test.util.util.concurrent.RunInThread;
import com.codeaffine.test.util.util.concurrent.RunInThreadRule;

@SuppressWarnings("restriction")
public class PgmResourceBundlePDETest {

  @Rule public final RunInThreadRule runInThreadRule = new RunInThreadRule();

  @Test
  @RunInThread
  public void testTranslationSucceedsIfNlsIsIntializedTwice() {
    new PgmResourceBundle().initialize();
    new PgmResourceBundle().initialize();

    CLIText cliText = CLIText.get();

    assertThat( cliText.alreadyOnBranch ).isNotNull();
  }

  @Test
  @RunInThread
  public void testTranslationSucceedsIfNlsIsIntialized() {
    new PgmResourceBundle().initialize();

    CLIText cliText = CLIText.get();

    assertThat( cliText.alreadyOnBranch ).isNotNull();
  }

  @RunInThread
  @Test(expected = TranslationBundleLoadingException.class)
  public void testTranslationFailsIfNlsNotInitialized() {
    CLIText.get();
  }

  @Before
  public void setUp() {
    new PgmResourceBundle().reset();
  }
}
