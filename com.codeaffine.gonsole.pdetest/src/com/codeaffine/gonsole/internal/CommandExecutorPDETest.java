package com.codeaffine.gonsole.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.pgm.TextBuiltin;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.base.Charsets;

public class CommandExecutorPDETest {

  @Rule
  public final TemporaryFolder tempFolder = new TemporaryFolder();

  private File gitDirectory;
  private ByteArrayOutputStream outputStream;
  private CommandExecutor executor;

  @Test
  public void testExecute() {
    CommandInfo commandInfo = new CommandLineParser().parse( "status" );

    executor.execute( commandInfo );

    String expectedOutput = new String( outputStream.toByteArray(), Charsets.UTF_8 );
    assertThat( expectedOutput ).startsWith( "# On branch master" );
  }

  @Test
  public void testExecuteWithException() {
    Exception exception = new Exception();
    CommandInfo commandInfo = new CommandInfo();
    commandInfo.command = new BadCommand( exception );

    try {
      executor.execute( commandInfo );
      fail();
    } catch( RuntimeException expected ) {
      assertThat( expected.getCause() ).isSameAs( exception );
    }
  }

  @Before
  public void setUp() throws Exception {
    new PgmResourceBundle().initialize();
    gitDirectory = createRepository();
    outputStream = new ByteArrayOutputStream();
    executor = new CommandExecutor( outputStream, gitDirectory );
  }

  private File createRepository() throws GitAPIException, IOException {
    File workDir = tempFolder.getRoot().getCanonicalFile();
    Git git = Git.init().setDirectory( workDir ).setBare( false ).call();
    Repository repository = git.getRepository();
    File result = repository.getDirectory().getCanonicalFile();
    repository.close();
    return result;
  }

  private static class BadCommand extends TextBuiltin {
    private final Exception exception;

    BadCommand( Exception exception ) {
      this.exception = exception;
    }

    @Override
    protected void run() throws Exception {
      throw exception;
    }
  }
}
