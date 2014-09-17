package com.kasisoft.libs.common.validation;

/**
 * Each implementor is used to check the validity of some input.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
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
