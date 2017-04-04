package com.kasisoft.libs.common.function;

import lombok.*;

import java.util.function.*;

/**
 * Collection of useful functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Functions {

  private Functions() {
  }
  
  /**
   * Protects the supplied function while testing for a null input.
   *  
   * @param delegate   The original function. Not <code>null</code>.
   * 
   * @return   The <code>null</code> aware function. <code>null</code> values will be passed through.
   *           Not <code>null</code>.
   */
  public static <T> Function<T, T> nullSafe( @NonNull Function<T, T> delegate ) {
    return $ -> $ != null ? delegate.apply($) : null;
  }
  
} /* ENDCLASS */
