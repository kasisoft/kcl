/**
 * Name........: Alignment
 * Description.: Values to specify an alignment. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import javax.swing.*;

/**
 * Values to specify an alignment.
 */
public enum Alignment {

  Left   ( SwingConstants.LEFT   ) ,
  Center ( SwingConstants.CENTER ) ,
  Right  ( SwingConstants.RIGHT  ) ;
  
  private int  xalignment;
  
  /**
   * Sets up this constant with an alignment value that is used for swing components. 
   * 
   * @param value   An alignment value that is used for swing components.
   */
  Alignment( int value ) {
    xalignment = value;
  }
  
  /**
   * Returns the alignment value that is used for swing components.
   *  
   * @return   The alignment value that is used for swing components.
   */
  public int getAlignmentX() {
    return xalignment;
  }
  
} /* ENDENUM */
