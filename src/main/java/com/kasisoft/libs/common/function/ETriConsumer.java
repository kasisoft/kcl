package com.kasisoft.libs.common.function;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface ETriConsumer<T1, T2, T3, E extends Exception> {

  void accept( T1 t1, T2 t2, T3 t3 ) throws E;
  
  default TriConsumer<T1, T2, T3> toTriConsumer() {
    return toTriConsumer( null );
  }
  
  default <RE extends RuntimeException> TriConsumer<T1, T2, T3> toTriConsumer( Function<Exception, RE> toRtException ) {
    return ($1, $2, $3 ) -> {
      try {
        accept( $1, $2, $3 );
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
