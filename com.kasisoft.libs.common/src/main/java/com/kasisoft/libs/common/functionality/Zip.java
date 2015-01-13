package com.kasisoft.libs.common.functionality;

/**
 * Combines two elements to a single new one.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated Will be removed with version 1.8 which will use Java 8.
 */
@Deprecated
public interface Zip<L,R,V> {

  /**
   * Combines the supplied elements to a single new element.
   * 
   * @param o1   The element on the left.
   * @param o2   The element on the right.
   * 
   * @return   A newly recombined element.
   */
  V zip( L o1, R o2 );
  
} /* ENDINTERFACE */
