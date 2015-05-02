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
  Right  ( SwingConstants.RIGHT  , TitledBorder.RIGHT   ) ,
  Top    ( SwingConstants.TOP    , TitledBorder.LEFT    ) ,
  Middle ( SwingConstants.CENTER , TitledBorder.LEFT    ) ,
  Bottom ( SwingConstants.BOTTOM , TitledBorder.LEFT    ) ;
  
  /**
   * @deprecated [03-May-2015:KASI]   Use {@link #getAlignment()} instead.
   */
  @Deprecated
  @Getter int   alignmentX;
  @Getter int   alignment;
  @Getter int   titledBorderX;
  
  /**
   * Sets up this constant with an alignment value that is used for swing components. 
   * 
   * @param value     An alignment value that is used for swing components.
   * @param borderx   The alignment for the titled border.
   */
  Alignment( int value, int borderx ) {
    alignmentX    = value;
    alignment     = value;
    titledBorderX = borderx;
  }
  
} /* ENDENUM */
