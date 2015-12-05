package com.codeaffine.gonsole.internal.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.Predicates;
import com.codeaffine.eclipse.core.runtime.RegistryAdapter;
import com.codeaffine.gonsole.internal.activator.IconRegistry;


public class ShowInGitConsoleCommandPDETest {

  @Test
  public void testCommandExtension() {
    Extension extension = readCommandExtension();

    assertThat( extension.getAttribute( "name" ) ).isNotEmpty();
    assertThat( extension.getAttribute( "description" ) ).isNotEmpty();
    assertThat( extension.getAttribute( "categoryId" ) ).isEqualTo( "org.eclipse.team.ui.category.team" );
    assertThat( extension.getAttribute( "defaultHandler" ) ).isNull();
  }

  @Test
  public void testHandlerExtension() {
    Extension extension = readHandlerExtension();

    assertThat( extension.createExecutableExtension( ShowInGitConsoleHandler.class ) ).isNotNull();
  }

  @Test
  public void testShowInDefinition() {
    Extension extension = new RegistryAdapter()
      .readExtension( "org.eclipse.core.expressions.definitions" )
      .thatMatches( Predicates.attribute( "id", "org.eclipse.ui.ide.showInDefinition" ) )
      .process();

    assertThat( extension ).isNotNull();
  }

  @Test
  public void testCommandImageExtnsion() {
    Extension extension = readCommandImageExtension();

    assertThat( extension.getAttribute( "icon" ) ).isEqualTo( IconRegistry.GONSOLE );
  }

  private static Extension readCommandExtension() {
    return new RegistryAdapter()
      .readExtension( "org.eclipse.ui.commands" )
      .thatMatches( Predicates.attribute( "id", ShowInGitConsoleHandler.COMMAND_ID ) )
      .process();
  }

  private static Extension readHandlerExtension() {
    return new RegistryAdapter()
      .readExtension( "org.eclipse.ui.handlers" )
      .thatMatches( Predicates.attribute( "commandId", ShowInGitConsoleHandler.COMMAND_ID ) )
      .process();
  }

  private static Extension readCommandImageExtension() {
    return new RegistryAdapter()
      .readExtension( "org.eclipse.ui.commandImages" )
      .thatMatches( Predicates.attribute( "commandId", OpenGitConsoleHandler.COMMAND_ID ) )
      .process();
  }

}
