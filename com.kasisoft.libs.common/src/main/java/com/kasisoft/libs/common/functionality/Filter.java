/**
 * Name........: Filter
 * Description.: Implementors are capable to decide whether an element can be accepted or not. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.functionality;

/**
 * Implementors are capable to decide whether an element can be accepted or not.
 */
public interface Filter<T> {

  /**
   * Returns <code>true</code> if the supplied input can be accepted.
   * 
   * @param input   The input which has to be tested.
   * 
   * @return   <code>true</code> <=> The input can be accepted.
   */
  boolean accept( T input );
  
} /* ENDINTERFACE */
