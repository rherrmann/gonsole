package com.codeaffine.console.core;


public class Proposal {

  private final String text;
  private final String info;

  public Proposal( String text, String info ) {
    this.text = text;
    this.info = info;
  }

  public String getText() {
    return text;
  }

  public String getInfo() {
    return info;
  }
}
