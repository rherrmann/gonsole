package com.codeaffine.gonsole.internal;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InOrder;

public class GitInterpreterTest {

  @Test
  public void testExecute() {
    String[] commandLine = new String[ 0 ];
    CommandInfo commandInfo = mock( CommandInfo.class );
    PgmResourceBundle pgmResourceBundle = mock( PgmResourceBundle.class );
    CommandExecutor commandExecutor = mock( CommandExecutor.class );
    CommandLineParser cmdLineParser = mock( CommandLineParser.class );
    when( cmdLineParser.parse( commandLine ) ).thenReturn( commandInfo );

    new GitInterpreter( pgmResourceBundle, commandExecutor, cmdLineParser ).execute( commandLine );

    InOrder order = inOrder( pgmResourceBundle, cmdLineParser, commandExecutor );
    order.verify( pgmResourceBundle ).initialize();
    order.verify( cmdLineParser ).parse( commandLine );
    order.verify( commandExecutor ).execute( commandInfo );
  }
}
