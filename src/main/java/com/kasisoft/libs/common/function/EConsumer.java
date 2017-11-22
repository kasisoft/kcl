package com.kasisoft.libs.common.function;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface EConsumer<T, E extends Exception> {

  void accept( T t ) throws E;

  default Consumer<T> toConsumer() {
    return toConsumer( null );
  }
  
  default <RE extends RuntimeException> Consumer<T> toConsumer( Function<Exception, RE> toRtException ) {
    return ($_) -> {
      try {
        accept( $_ );
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
