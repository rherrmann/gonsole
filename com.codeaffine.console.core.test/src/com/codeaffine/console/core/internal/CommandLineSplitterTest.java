package com.codeaffine.console.core.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CommandLineSplitterTest {

  @Test
  public void testSplit() {
    String[] commandLine = new CommandLineSplitter( "a b c" ).split();

    assertThat( commandLine ).containsExactly( "a", "b", "c" );
  }

  @Test
  public void testSplitWithArgumentLessCommand() {
    String[] commandLine = new CommandLineSplitter( "a" ).split();

    assertThat( commandLine ).containsExactly( "a" );
  }

  @Test
  public void testSplitWithTabs() {
    String[] commandLine = new CommandLineSplitter( "a\tb\tc" ).split();

    assertThat( commandLine ).containsExactly( "a", "b", "c" );
  }

  @Test
  public void testSplitWithMultipleSpaces() {
    String[] commandLine = new CommandLineSplitter( "a  b" ).split();

    assertThat( commandLine ).containsExactly( "a", "b" );
  }

  @Test
  public void testSplitWithTrailingSpaces() {
    String[] commandLine = new CommandLineSplitter( "a  b  " ).split();

    assertThat( commandLine ).containsExactly( "a", "b" );
  }

  @Test
  public void testSplitWithLeadingSpaces() {
    String[] commandLine = new CommandLineSplitter( "  a  b" ).split();

    assertThat( commandLine ).containsExactly( "a", "b" );
  }

  @Test
  public void testSplitWithGitPrefix() {
    String[] commandLine = new CommandLineSplitter( "git foo" ).split();

    assertThat( commandLine ).containsExactly( "foo" );
  }

  @Test
  public void testSplitWithOnlyGitPrefix() {
    String[] commandLine = new CommandLineSplitter( "git" ).split();

    assertThat( commandLine ).containsExactly( "git" );
  }
}
