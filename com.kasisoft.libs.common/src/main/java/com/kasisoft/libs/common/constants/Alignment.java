package com.kasisoft.libs.common.constants;

import javax.swing.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Values to specify an alignment.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Alignment {

  Left   ( SwingConstants.LEFT   ) ,
  Center ( SwingConstants.CENTER ) ,
  Right  ( SwingConstants.RIGHT  ) ;
  
  @Getter int  alignmentX;
  
  /**
   * Sets up this constant with an alignment value that is used for swing components. 
   * 
   * @param value   An alignment value that is used for swing components.
   */
  Alignment( int value ) {
    alignmentX = value;
  }
  
} /* ENDENUM */
