package com.codeaffine.console.core;

import org.eclipse.jface.resource.ImageDescriptor;


public class Proposal {

  private final String text;
  private final String info;
  private final ImageDescriptor imageDescriptor;
  private final Comparable<?> sortKey;

  public Proposal( Comparable<?> sortKey, String text, String info, ImageDescriptor imageDescriptor ) {
    this.sortKey = sortKey;
    this.text = text;
    this.info = info;
    this.imageDescriptor = imageDescriptor;
  }

  public String getText() {
    return text;
  }

  public String getInfo() {
    return info;
  }

  public ImageDescriptor getImageDescriptor() {
    return imageDescriptor;
  }

  public Comparable<?> getSortKey() {
    return sortKey;
  }
}
