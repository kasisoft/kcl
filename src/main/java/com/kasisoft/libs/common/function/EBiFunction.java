package com.kasisoft.libs.common.function;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface EBiFunction<T, U, R, E extends Exception> {

  R apply( T t, U u ) throws E;
  
  default BiFunction<T, U, R> toBiFunction() {
    return toBiFunction( null );
  }
  
  default <RE extends RuntimeException> BiFunction<T, U, R> toBiFunction( Function<Exception, RE> toRtException ) {
    return ($1, $2) -> {
      try {
        return apply( $1, $2 );
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
