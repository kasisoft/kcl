package com.kasisoft.libs.common.functionality;

/**
 * Implementors are capable to decide whether an element can be accepted or not.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated Will be removed with version 1.8 which will use Java 8.
 */
@Deprecated
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
