package com.codeaffine.gonsole.internal;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.codeaffine.console.core.Console;
import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.gonsole.internal.activator.IconRegistry;
import com.codeaffine.gonsole.internal.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.test.util.concurrency.ConcurrentHelper;

public class GitConsoleConfigurerPDETest {

  private static final String NO_REPOSITORY_SELECTED
    = "No repository selected, type 'use <repository>' to change the current repository";

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private CompositeRepositoryProvider repositoryProvider;
  private Console console;
  private GitConsoleConfigurer configurer;

  @Test
  public void testConfigure() {
    configurer.configure( console );

    verify( console ).setTitle( NO_REPOSITORY_SELECTED );
    verify( console ).setImageDescriptor( new IconRegistry().getDescriptor( IconRegistry.GONSOLE ) );
    verify( console ).setColorScheme( isA( GitConsoleColorScheme.class ) );
    verify( console ).setConsoleComponentFactory( isA( GitConsoleComponentFactory.class ) );
  }

  @Test
  public void testChangeCurrentRepositoryUpdatesTitle() {
    configurer.configure( console );

    repositoryProvider.setCurrentRepositoryLocation( new File( "/path/to/repo/.git" ) );
    flushPendingEvents();

    verify( console ).setTitle( "Git Console: repo" );
  }

  @Test
  public void testChangeCurrentRepositoryFromBackgroundThread() {
    AtomicReference<Thread> invocationThread = trackSetTitleInvocationThread();
    configurer.configure( console );

    changeCurrentRepositoryInBackgroundThread();

    assertThat( invocationThread.get() ).isEqualTo( displayHelper.getDisplay().getThread() );
  }

  @Before
  public void setUp() {
    console = mock( Console.class );
    Display display = displayHelper.getDisplay();
    repositoryProvider = new CompositeRepositoryProvider();
    configurer = new GitConsoleConfigurer( display, repositoryProvider );
  }

  private AtomicReference<Thread> trackSetTitleInvocationThread() {
    final AtomicReference<Thread> result = new AtomicReference<Thread>();
    doAnswer( new Answer<Object>() {
      @Override
      public Object answer( InvocationOnMock invocation ) {
        result.set( Thread.currentThread() );
        return null;
      }
    } ).when( console ).setTitle( anyString() );
    return result;
  }

  private void changeCurrentRepositoryInBackgroundThread() {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        repositoryProvider.setCurrentRepositoryLocation( new File( "/path/to/repo/.git" ) );
      }
    };
    ConcurrentHelper.runInThread( runnable );
    flushPendingEvents();
  }
}
