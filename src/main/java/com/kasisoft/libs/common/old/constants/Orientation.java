package com.kasisoft.libs.common.old.constants;

import javax.swing.JSplitPane;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Constants used to specify orientations for various purposes.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Orientation {
  
  Horizontal ( JSplitPane.HORIZONTAL_SPLIT ),
  Vertical   ( JSplitPane.VERTICAL_SPLIT   );
  
  @Getter int   splitPaneOrientation;
  
  Orientation( int splitpane ) {
    splitPaneOrientation = splitpane;
  }
  
} /* ENDENUM */
