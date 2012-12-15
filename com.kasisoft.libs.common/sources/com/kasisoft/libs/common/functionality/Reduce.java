/**
 * Name........: Reduce
 * Description.: Reduces an input together with a current element. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.functionality;

/**
 * Reduces an input together with a current element.
 */
public interface Reduce<F,T> {

  /**
   * Reduces an element while performing an operation with an initial value.
   * 
   * @param input     The input object which has to be reduced.
   * @param initial   The initial value.
   * 
   * @return   The reduced value.
   */
  T reduce( F input, T initial );
  
} /* ENDINTERFACE */
