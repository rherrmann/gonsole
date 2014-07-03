package com.codeaffine.console.core.internal.contentassist;

import static com.codeaffine.console.core.internal.contentassist.ConsoleInformationControlCreator.Appearance.FIXED;
import static com.codeaffine.console.core.internal.contentassist.ConsoleInformationControlCreator.Appearance.RESIZABLE;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.test.util.swt.DisplayHelper;

public class ConsoleInformationControlCreatorPDETest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();
  private Shell parentShell;

  @Test
  public void testCreateFixed() {
    ConsoleInformationControlCreator creator = new ConsoleInformationControlCreator( FIXED );
    creator.createInformationControl( parentShell );

    assertThat( getInfoShell().getStyle() & SWT.RESIZE ).isZero();
  }

  @Test
  public void testCreateResizable() {
    ConsoleInformationControlCreator creator = new ConsoleInformationControlCreator( RESIZABLE );
    creator.createInformationControl( parentShell );

    assertThat( getInfoShell().getStyle() & SWT.RESIZE ).isNotZero();
  }

  @Test
  public void testFont() {
    ConsoleInformationControlCreator creator = new ConsoleInformationControlCreator( FIXED );
    creator.createInformationControl( parentShell );

    assertThat( getStyledText().getFont() ).isSameAs( JFaceResources.getTextFont() );
  }

  @Before
  public void setUp() {
    parentShell = displayHelper.createShell();
  }

  private StyledText getStyledText() {
    Composite composite = ( Composite )getInfoShell().getChildren()[ 0 ];
    StyledText styledText = ( StyledText )composite.getChildren()[ 0 ];
    return styledText;
  }

  private Shell getInfoShell() {
    return parentShell.getShells()[ 0 ];
  }
}
