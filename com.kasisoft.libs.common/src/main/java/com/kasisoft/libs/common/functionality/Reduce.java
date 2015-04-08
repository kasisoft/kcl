package com.kasisoft.libs.common.functionality;

/**
 * Reduces an input together with a current element.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated Will be removed with version 1.8 which will use Java 8.
 */
@Deprecated
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
