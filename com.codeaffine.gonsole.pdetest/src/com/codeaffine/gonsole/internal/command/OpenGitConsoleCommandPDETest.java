package com.codeaffine.gonsole.internal.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.core.commands.IHandler;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.Predicates;
import com.codeaffine.eclipse.core.runtime.RegistryAdapter;
import com.codeaffine.gonsole.internal.activator.IconRegistry;


public class OpenGitConsoleCommandPDETest {

  @Test
  public void testCommandExtension() {
    Extension extension = readCommandExtension();

    assertThat( extension.getAttribute( "name" ) ).isNotEmpty();
    assertThat( extension.getAttribute( "description" ) ).isNotEmpty();
    assertThat( extension.getAttribute( "categoryId" ) ).isEqualTo( "org.eclipse.team.ui.category.team" );
    IHandler handler = extension.createExecutableExtension( "defaultHandler", IHandler.class );
    assertThat( handler ).isInstanceOf( OpenGitConsoleHandler.class );
  }

  @Test
  public void testCommandImageExtnsion() {
    Extension extension = readCommandImageExtension();

    assertThat( extension.getAttribute( "icon" ) ).isEqualTo( IconRegistry.GONSOLE );
  }

  private static Extension readCommandExtension() {
    return new RegistryAdapter()
      .readExtension( "org.eclipse.ui.commands" )
      .thatMatches( Predicates.attribute( "id", OpenGitConsoleHandler.COMMAND_ID ) )
      .process();
  }

  private static Extension readCommandImageExtension() {
    return new RegistryAdapter()
      .readExtension( "org.eclipse.ui.commandImages" )
      .thatMatches( Predicates.attribute( "commandId", OpenGitConsoleHandler.COMMAND_ID ) )
      .process();
  }

}
