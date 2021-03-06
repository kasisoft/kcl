package com.kasisoft.libs.common.functional;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface TriFunction<T1, T2, T3, R> {

  R apply(T1 t1, T2 t2, T3 t3);
  
} /* ENDINTERFACE */
