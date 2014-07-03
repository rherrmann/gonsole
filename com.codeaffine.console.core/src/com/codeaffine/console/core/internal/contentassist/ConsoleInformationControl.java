package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.ConsoleInformationControlCreator.Appearance.RESIZABLE;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.codeaffine.console.core.internal.contentassist.ConsoleInformationControlCreator.Appearance;


class ConsoleInformationControl extends DefaultInformationControl {

  static IInformationControlCreator createInformationControlCreator( Appearance appearance ) {
    return new ConsoleInformationControlCreator( appearance );
  }

  ConsoleInformationControl( Shell shell ) {
    super( shell );
  }

  ConsoleInformationControl( Shell shell, IInformationPresenter presenter ) {
    super( shell, ( ToolBarManager )null, presenter );
  }

  void setFont( Font font ) {
    StyledText styledText = getStyledText();
    styledText.setFont( font );
  }

  @Override
  public IInformationControlCreator getInformationPresenterControlCreator() {
    return createInformationControlCreator( RESIZABLE );
  }

  private StyledText getStyledText() {
    Composite composite = ( Composite )getShell().getChildren()[ 0 ];
    return ( StyledText )composite.getChildren()[ 0 ];
  }
}