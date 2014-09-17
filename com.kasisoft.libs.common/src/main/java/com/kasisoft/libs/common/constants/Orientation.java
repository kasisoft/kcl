package com.kasisoft.libs.common.constants;

import javax.swing.*;

/**
 * Constants used to specify orientations for various purposes.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public enum Orientation {
  
  Horizontal ( JSplitPane.HORIZONTAL_SPLIT ),
  Vertical   ( JSplitPane.VERTICAL_SPLIT   );
  
  private int   orientation;
  
  Orientation( int splitpane ) {
    orientation = splitpane;
  }

  /**
   * Returns the orientation for a JSplitPane.
   * 
   * @return   The orientation for a JSplitPane.
   */
  public int getSplitPaneOrientation() {
    return orientation;
  }
  
} /* ENDENUM */
