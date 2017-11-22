package com.kasisoft.libs.common.function;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface EBiConsumer<T, U, E extends Exception> {

  void accept( T t, U u ) throws E;

  default BiConsumer<T, U> toBiConsumer() {
    return toBiConsumer( null );
  }
  
  default <RE extends RuntimeException> BiConsumer<T, U> toBiConsumer( Function<Exception, RE> toRtException ) {
    return ($1, $2) -> {
      try {
        accept( $1, $2 );
      } catch( Exception ex ) {
        if( toRtException != null ) {
          throw toRtException.apply( ex );
        } else {
          throw new RuntimeException(ex);
        }
      }
    };
  }
  
} /* ENDINTERFACE */
