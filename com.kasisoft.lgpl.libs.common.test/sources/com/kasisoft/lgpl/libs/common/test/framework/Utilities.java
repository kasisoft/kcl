/**
 * Name........: Utilities
 * Description.: Collection of utility functions.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.framework;

import java.util.*;

/**
 * Collection of utility functions.
 */
public class Utilities {

  public static final List<Integer> toList( int ... args ) {
    List<Integer> result = new ArrayList<Integer>();
    for( int value : args ) {
      result.add( Integer.valueOf( value ) );
    }
    return result;
  }
  

} /* ENDCLASS */
