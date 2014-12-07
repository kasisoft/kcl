package com.kasisoft.libs.common.constants;

import javax.swing.*;

import lombok.*;
import lombok.experimental.*;

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
