package com.codeaffine.console.core.pdetest.bot;

import static com.codeaffine.test.util.swt.SWTEventHelper.trigger;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.console.TextConsoleViewer;

import com.google.common.base.Predicate;

class ConsolePageBot {

  private final TextConsolePage consolePage;
  private final TextConsoleViewer viewer;
  private final StyledText styledText;
  private final TextViewerChangeObserver textViewerChangeObserver;
  private String capturedText;

  ConsolePageBot( TextConsolePage consolePage ) {
    this.consolePage = consolePage;
    this.styledText = ( StyledText )consolePage.getControl();
    this.viewer = consolePage.getViewer();
    this.textViewerChangeObserver = new TextViewerChangeObserver( viewer );
  }

  void waitForInitialPrompt() {
    textViewerChangeObserver.waitForChange( "" );
    captureText();
  }

  void waitForCommandLineProcessing() {
    textViewerChangeObserver.waitForChange( capturedText );
  }

  void waitForColoring( int offset, RGB rgb ) {
    textViewerChangeObserver.waitForColoring( offset, rgb );
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

  void enterCommandLine( String commandLine ) {
    String textToEnter = commandLine + getLineDelimiter();
    captureText( textToEnter );
    styledText.append( textToEnter );
  }

  void triggerEvent( int eventType, int modifiers, char character ) {
    trigger( eventType )
      .withKeyCode( character )
      .withStateMask( modifiers )
      .withCharacter( character )
      .on( styledText );
    captureText();
  }

  void triggerEvent( int eventType, int modifiers, int keyCode ) {
    trigger( eventType )
      .withKeyCode( keyCode )
      .withStateMask( modifiers )
      .on( styledText );
    captureText();
  }

  void selectText( int start, int length ) {
    styledText.setSelectionRange( start, length );
  }

  void runToolBarAction( final String text ) {
    IAction[] actions = getToolBarActions();
    IAction action = find( Arrays.asList( actions ), new Predicate<IAction>() {
      @Override
      public boolean apply( IAction input ) {
        return input.getText().replaceAll( "&", "" ).equals( text );
      }
    } );
    action.run();
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
    return toArray( actions, IAction.class );
  }

  private void captureText() {
    captureText( "" );
  }

  private void captureText( String suffix ) {
    capturedText = styledText.getText() + suffix;
  }
}