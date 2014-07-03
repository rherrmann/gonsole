package com.codeaffine.console.core.internal.contentassist;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.widgets.Shell;

class ConsoleInformationControlCreator implements IInformationControlCreator {

  enum Appearance {
    FIXED,
    RESIZABLE
  }

  private final Appearance appearance;

  ConsoleInformationControlCreator( Appearance appearance ) {
    this.appearance = appearance;
  }

  @Override
  public IInformationControl createInformationControl( Shell parent ) {
    ConsoleInformationControl result;
    if( appearance == Appearance.FIXED ) {
      result = new ConsoleInformationControl( parent );
    } else {
      result = new ConsoleInformationControl( parent, null );
    }
    result.setFont( JFaceResources.getTextFont() );
    return result;
  }
}