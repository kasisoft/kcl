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
  
  @Getter int   alignment;
  @Getter int   titledBorderX;
  
  /**
   * Sets up this constant with an alignment value that is used for swing components. 
   * 
   * @param value     An alignment value that is used for swing components.
   * @param borderx   The alignment for the titled border.
   */
  Alignment( int value, int borderx ) {
    alignment     = value;
    titledBorderX = borderx;
  }
  
  public void set( @NonNull JComponent component ) {
    if( component instanceof JLabel ) {
      ((JLabel) component).setHorizontalAlignment( alignment );
    } else if( component instanceof JTextField ) {
      ((JTextField) component).setHorizontalAlignment( alignment );
    }
  }

} /* ENDENUM */
