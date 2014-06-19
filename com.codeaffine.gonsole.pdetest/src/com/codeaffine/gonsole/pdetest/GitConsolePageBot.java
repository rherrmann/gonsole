package com.codeaffine.gonsole.pdetest;

import static com.codeaffine.test.util.swt.SWTEventHelper.trigger;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.console.TextConsoleViewer;

import com.google.common.collect.Iterables;

class GitConsolePageBot {

  private final TextConsolePage consolePage;
  private final TextConsoleViewer viewer;
  private final StyledText styledText;
  private final StyleRangeCollector styleRangeCollector;

  GitConsolePageBot( TextConsolePage consolePage ) {
    this.consolePage = consolePage;
    this.styledText = ( StyledText )consolePage.getControl();
    this.viewer = consolePage.getViewer();
    this.styleRangeCollector = new StyleRangeCollector( styledText );
  }

  void waitForChange() {
    new TextViewerChangeObserver( viewer ).waitForChange();
  }

  int getCharCount() {
    return styledText.getCharCount();
  }

  void setCaretOffset( int caretOffset ) {
    styledText.setCaretOffset( caretOffset );
  }

  int getCaretOffset() {
    return styledText.getCaretOffset();
  }

  String getText() {
    return styledText.getText();
  }

  String getLineDelimiter() {
    return styledText.getLineDelimiter();
  }

  Color getForegroundAt( final int offset ) {
    return styleRangeCollector.getStyleRangeAt( offset ).foreground;
  }

  void append( String text ) {
    styledText.append( text );
  }

  void triggerEvent( int eventType, int modifiers, char character ) {
    trigger( eventType )
      .withKeyCode( character )
      .withStateMask( modifiers )
      .withCharacter( character )
      .on( styledText );
  }

  void selectText( int start, int length ) {
    styledText.setSelectionRange( start, length );
  }

  void runToolBarAction( String text ) {
    IAction[] actions = getToolBarActions();
    for( IAction action : actions ) {
      if( action.getText().replaceAll( "&", "" ).equals( text ) ) {
        action.run();
      }
    }
  }

  IAction[] getToolBarActions() {
    IActionBars actionBars = consolePage.getSite().getActionBars();
    IContributionItem[] items = actionBars.getToolBarManager().getItems();
    Collection<IAction> actions = newArrayList();
    for( IContributionItem item : items ) {
      if( item instanceof ActionContributionItem ) {
        ActionContributionItem actionItem = ( ActionContributionItem )item;
        actions.add( actionItem.getAction() );
      }
    }
    return Iterables.toArray( actions, IAction.class );
  }
}