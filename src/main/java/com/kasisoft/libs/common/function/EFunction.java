package com.kasisoft.libs.common.function;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [21-JUL-2018:KASI]   Will be removed with the next version.
 */
@Deprecated
@FunctionalInterface
public interface EFunction<T, R, E extends Exception> {

  R apply( T t ) throws E;
  
  default Function<T, R> toFunction() {
    return toFunction( null );
  }
  
  default <RE extends RuntimeException> Function<T, R> toFunction( Function<Exception, RE> toRtException ) {
    return ($_) -> {
      try {
        return apply( $_ );
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
