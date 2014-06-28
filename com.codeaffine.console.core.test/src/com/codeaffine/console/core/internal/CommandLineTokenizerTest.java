package com.codeaffine.console.core.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CommandLineTokenizerTest {

  @Test
  public void testTokenize() {
    String[] tokens = new CommandLineTokenizer( "a b c" ).tokenize();

    assertThat( tokens ).containsExactly( "a", "b", "c" );
  }

  @Test
  public void testTokenizeWithEmptyString() {
    String[] tokens = new CommandLineTokenizer( "" ).tokenize();

    assertThat( tokens ).isEmpty();
  }

  @Test
  public void testTokenizeWithArgumentLessCommand() {
    String[] tokens = new CommandLineTokenizer( "a" ).tokenize();

    assertThat( tokens ).containsExactly( "a" );
  }

  @Test
  public void testTokenizeWithTabs() {
    String[] tokens = new CommandLineTokenizer( "a\tb\tc" ).tokenize();

    assertThat( tokens ).containsExactly( "a", "b", "c" );
  }

  @Test
  public void testTokenizeWithMultipleSpaces() {
    String[] tokens = new CommandLineTokenizer( "a  b" ).tokenize();

    assertThat( tokens ).containsExactly( "a", "b" );
  }

  @Test
  public void testTokenizeWithTrailingSpaces() {
    String[] tokens = new CommandLineTokenizer( "a  b  " ).tokenize();

    assertThat( tokens ).containsExactly( "a", "b" );
  }

  @Test
  public void testTokenizeWithLeadingSpaces() {
    String[] tokens = new CommandLineTokenizer( "  a  b" ).tokenize();

    assertThat( tokens ).containsExactly( "a", "b" );
  }

  @Test
  public void testTokenizeWithGitPrefix() {
    String[] tokens = new CommandLineTokenizer( "git foo" ).tokenize();

    assertThat( tokens ).containsExactly( "foo" );
  }

  @Test
  public void testTokenizeWithOnlyGitPrefix() {
    String[] tokens = new CommandLineTokenizer( "git" ).tokenize();

    assertThat( tokens ).containsExactly( "git" );
  }

  @Test
  public void testTokenizeWithGitToken() {
    String[] tokens = new CommandLineTokenizer( "foo git bar" ).tokenize();

    assertThat( tokens ).containsExactly( "foo", "git", "bar" );
  }

  @Test
  public void testTokenizeWithQuotedArgument() {
    String[] tokens = new CommandLineTokenizer( "commit -m \"a short message\"" ).tokenize();

    assertThat( tokens ).containsExactly( "commit", "-m", "a short message" );
  }

  @Test
  public void testTokenizeWithOpenQuote() {
    String[] tokens = new CommandLineTokenizer( "foo \"bar" ).tokenize();

    assertThat( tokens ).containsExactly( "foo", "bar" );
  }

  @Test
  public void testTokenizeWithEscapedQuotes() {
    String[] tokens = new CommandLineTokenizer( "foo \\\"" ).tokenize();

    assertThat( tokens ).containsExactly( "foo", "\"" );
  }

  @Test
  public void testTokenizeWithEscapedQuotesInQuotedArgument() {
    String[] tokens = new CommandLineTokenizer( "foo \"\\\"\"" ).tokenize();

    assertThat( tokens ).containsExactly( "foo", "\"" );
  }

  @Test
  public void testTokenizeWithDoubleBackslash() {
    String[] tokens = new CommandLineTokenizer( "foo\\\\bar" ).tokenize();

    assertThat( tokens ).containsExactly( "foo\\bar" );
  }

  @Test
  public void testTokenizeWithTrailingBackslash() {
    String[] tokens = new CommandLineTokenizer( "foo \\" ).tokenize();

    assertThat( tokens ).containsExactly( "foo", "\\" );
  }

  @Test
  public void testTokenizeTwice() {
    CommandLineTokenizer tokenizer = new CommandLineTokenizer( "foo" );

    String[] tokens1 = tokenizer.tokenize();
    String[] tokens2 = tokenizer.tokenize();

    assertThat( tokens1 ).containsExactly( "foo" );
    assertThat( tokens2 ).containsExactly( "foo" );
  }
}
