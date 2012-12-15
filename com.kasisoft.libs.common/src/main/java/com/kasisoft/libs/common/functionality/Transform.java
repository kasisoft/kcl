/**
 * Name........: Transform
 * Description.: Transforms an input into a specific output. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.functionality;

/**
 * Transforms an input into a specific output.
 */
public interface Transform<F,T> {

  /**
   * Maps a specific output for the supplied input.
   * 
   * @param input   The input object which has to be mapped.
   * 
   * @return   The mapped object.
   */
  T map( F input );
  
} /* ENDINTERFACE */
