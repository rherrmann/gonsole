package com.codeaffine.gonsole.acceptance;

import static com.codeaffine.console.core.pdetest.bot.ConsoleAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.console.core.internal.GenericConsole;
import com.codeaffine.console.core.pdetest.bot.ConsoleBot;
import com.codeaffine.gonsole.internal.GitConsoleConfigurer;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.test.resources.ProjectHelper;


public class GitConsoleRefreshPDETest {

  @Rule public final ConsoleHelper configurer = new ConsoleHelper();
  @Rule public final ConsoleBot consoleBot = new ConsoleBot();

  private ProjectHelper projectHelper;
  private GenericConsole console;

  @Test
  public void testRefreshResourcesAfterCheckout() throws Exception {
    createSharedProject();
    IFile file = projectHelper.createFile( "file", "initial content" );
    commit();
    createBranch( "topic" );
    checkoutBranch( "topic" );
    projectHelper.createFile( "file", "changed on topic" );
    commit();

    consoleBot.enterCommandLine( "checkout master" );

    assertThat( consoleBot )
      .hasProcessedCommandLine();
    assertThat( file.isSynchronized( IResource.DEPTH_ZERO ) ).isTrue();
  }

  @Before
  public void setUp() {
    console = consoleBot.open( configurer.createConfigurer( "repo" ) );
  }

  @After
  public void tearDown() throws CoreException {
    if( projectHelper != null ) {
      projectHelper.dispose();
    }
  }

  private void createBranch( String branchName ) throws IOException, GitAPIException {
    Repository repository = openCurrentRepository();
    new Git( repository ).branchCreate().setName( branchName ).call();
    repository.close();
  }

  private void checkoutBranch( String branchName ) throws IOException, GitAPIException {
    Repository repository = openCurrentRepository();
    new Git( repository ).checkout().setName( branchName ).call();
    repository.close();
  }

  private void commit() throws IOException, GitAPIException {
    Repository repository = openCurrentRepository();
    new Git( repository ).add().addFilepattern( "." ).call();
    new Git( repository ).commit().setMessage( "Commit all changes" ).call();
    repository.close();
  }

  private void createSharedProject() throws IOException {
    Repository repository = openCurrentRepository();
    File repositoryWorkDir = repository.getWorkTree();
    repository.close();
    projectHelper = new ProjectHelper( repositoryWorkDir );
  }

  private Repository openCurrentRepository() throws IOException {
    FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
    repositoryBuilder.setMustExist( true );
    repositoryBuilder.setGitDir( getCurrentRepositoryLocation() );
    return repositoryBuilder.build();
  }

  private File getCurrentRepositoryLocation() {
    GitConsoleConfigurer consoleConfigurer = ( GitConsoleConfigurer )console.getConsoleConfigurer();
    CompositeRepositoryProvider repositoryProvider = consoleConfigurer.getRepositoryProvider();
    return repositoryProvider.getCurrentRepositoryLocation();
  }

}
