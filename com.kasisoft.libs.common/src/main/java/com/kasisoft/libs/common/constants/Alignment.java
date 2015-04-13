package com.kasisoft.libs.common.constants;

import javax.swing.*;
import javax.swing.border.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Values to specify an alignment.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Alignment {

  Left   ( SwingConstants.LEFT   , TitledBorder.LEFT    ) ,
  Center ( SwingConstants.CENTER , TitledBorder.CENTER  ) ,
  Right  ( SwingConstants.RIGHT  , TitledBorder.RIGHT   ) ;
  
  @Getter int   alignmentX;
  @Getter int   titledBorderX;
  
  /**
   * Sets up this constant with an alignment value that is used for swing components. 
   * 
   * @param value     An alignment value that is used for swing components.
   * @param borderx   The alignment for the titled border.
   */
  Alignment( int value, int borderx ) {
    alignmentX    = value;
    titledBorderX = borderx;
  }
  
} /* ENDENUM */
