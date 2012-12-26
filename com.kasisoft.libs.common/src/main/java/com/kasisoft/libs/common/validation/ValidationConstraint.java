/**
 * Name........: ValidationConstraint
 * Description.: Each implementor is used to check the validity of some input.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.validation;

/**
 * Each implementor is used to check the validity of some input.
 */
public interface ValidationConstraint<T> {

  /**
   * Returns <code>true</code> if the supplied input could be recognized as valid.
   * 
   * @param input   The input that has to be checked.
   * 
   * @return   <code>true</code> <=> The supplied input is considered valid.
   */
  boolean check( T input );
  
} /* ENDINTERFACE */
