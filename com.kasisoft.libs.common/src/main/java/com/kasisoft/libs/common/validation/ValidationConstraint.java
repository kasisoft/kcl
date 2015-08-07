package com.kasisoft.libs.common.validation;

import java.util.function.*;

/**
 * Each implementor is used to check the validity of some input.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [07-Aug-2015:KASI]   Will be removed with version 1.9 as it will be replaced by {@link Predicate}.
 */
@Deprecated
public interface ValidationConstraint<T> extends Predicate<T> {

  /**
   * Returns <code>true</code> if the supplied input could be recognized as valid.
   * 
   * @param input   The input that has to be checked.
   * 
   * @return   <code>true</code> <=> The supplied input is considered valid.
   */
  default boolean check( T input ) {
    return test( input );
  }
  
} /* ENDINTERFACE */
