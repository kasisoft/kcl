package com.kasisoft.libs.common.function;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [21-JUL-2018:KASI]   Will be removed with the next version.
 */
@Deprecated
@FunctionalInterface
public interface ETriFunction<T1, T2, T3, R, E extends Exception> {

  R apply( T1 t1, T2 t2, T3 t3 ) throws E;
  
  default TriFunction<T1, T2, T3, R> toTriFunction() {
    return toTriFunction( null );
  }
  
  default <RE extends RuntimeException> TriFunction<T1, T2, T3, R> toTriFunction( Function<Exception, RE> toRtException ) {
    return ($1, $2, $3) -> {
      try {
        return apply( $1, $2, $3 );
      } catch( Exception ex ) {
        if( toRtException != null ) {
          throw toRtException.apply(ex);
        } else {
          throw new RuntimeException(ex);
        }
      }
    };
  }

} /* ENDINTERFACE */
