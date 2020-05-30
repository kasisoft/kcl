package com.kasisoft.libs.common.old.function;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface TriFunction<T1, T2, T3, R> {

  R apply( T1 t1, T2 t2, T3 t3 );
  
} /* ENDINTERFACE */
