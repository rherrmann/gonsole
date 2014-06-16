package com.codeaffine.gonsole.pdetest;

import static com.codeaffine.test.util.swt.SWTEventHelper.trigger;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.console.TextConsoleViewer;

class GitConsolePageBot {

  private final TextConsoleViewer viewer;
  private final StyledText styledText;
  private final StyleRangeCollector styleRangeCollector;

  GitConsolePageBot( TextConsolePage textConsolePage ) {
    styledText = ( StyledText )textConsolePage.getControl();
    viewer = textConsolePage.getViewer();
    styleRangeCollector = new StyleRangeCollector( styledText );
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

  void triggerEvent( int eventType, char character ) {
     trigger( eventType ).withKeyCode( character ).withCharacter( character ).on( styledText );
  }
}