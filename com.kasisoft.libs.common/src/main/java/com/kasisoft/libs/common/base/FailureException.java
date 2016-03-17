package com.kasisoft.libs.common.base;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

/**
 * Specialisation of the RuntimeException which provides a numerical code which allows to handle this exception in a 
 * more apropriate way than checking it's message.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Getter @FieldDefaults(level = AccessLevel.PRIVATE)
public class FailureException extends RuntimeException {

  static final Object[] NO_PARAMS = new Object[0];
  
  FailureCode   failurecode;
  Object[]      params;

  FailureException( String message, FailureCode code, Throwable cause, Object[] parameters ) {
    super( message, cause );
    failurecode = code;
    params      = parameters;
  }
  
  public static FailureException wrap( Exception ex ) {
    if( ex instanceof FailureException ) {
      return (FailureException) ex;
    } else {
      return new FailureException( ex.getLocalizedMessage(), FailureCode.Unexpected, ex, NO_PARAMS );
    }
  }

  public static <T> Consumer<T> ensure( Consumer<T> consumer ) {
    return new Consumer<T>() {

      @Override
      public void accept( T t ) {
        try {
          consumer.accept(t);
        } catch( Exception ex ) {
          throw wrap(ex);
        }
      }

    };
  }

  public static <T1,T2> BiConsumer<T1,T2> ensure( BiConsumer<T1,T2> consumer ) {
    return new BiConsumer<T1,T2>() {

      @Override
      public void accept( T1 t1, T2 t2 ) {
        try {
          consumer.accept( t1, t2 );
        } catch( Exception ex ) {
          throw wrap(ex);
        }
      }

    };
  }

  public static <S,T> Function<S,T> ensure( Function<S,T> function ) {
    return new Function<S, T>(){

      @Override
      public T apply( S s ) {
        try {
          return function.apply( s );
        } catch( Exception ex ) {
          throw wrap( ex );
        }
      }

    };
  }

  public static <S,T,R> BiFunction<S,T,R> ensure( BiFunction<S,T,R> function ) {
    return new BiFunction<S,T,R>() {

      @Override
      public R apply( S s, T t ) {
        try {
          return function.apply( s, t );
        } catch( Exception ex ) {
          throw wrap( ex );
        }
      }

    };
  }
  
} /* ENDCLASS */
