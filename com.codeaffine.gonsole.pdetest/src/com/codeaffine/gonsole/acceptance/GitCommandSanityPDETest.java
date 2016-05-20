package com.codeaffine.gonsole.acceptance;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.eclipse.jgit.pgm.CommandRef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.codeaffine.gonsole.internal.contentassist.CommandRefCollector;
import com.codeaffine.gonsole.internal.interpreter.CommandLineParser;

@RunWith(Parameterized.class)
public class GitCommandSanityPDETest {

  @Parameters(name = "{0}")
  public static Collection<String> parameters() {
    return new CommandRefCollector().collect().stream().map( CommandRef::getName ).collect( toList() );
  }

  @Parameter()
  public String commandName;

  @Test
  public void testGetUsage() {
    String usage = new CommandLineParser().getUsage( commandName );

    assertThat( usage ).isNotEmpty();
  }

}
