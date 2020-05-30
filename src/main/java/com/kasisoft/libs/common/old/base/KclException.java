package com.kasisoft.libs.common.old.base;

import java.util.function.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Specialisation of the RuntimeException which is commonly used within this library.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Getter @FieldDefaults(level = AccessLevel.PRIVATE)
public class KclException extends RuntimeException {

  public KclException() {
  }
  
  public KclException( String message ) {
    super( message );
  }
  
  KclException( String message, Throwable cause ) {
    super( message, cause );
  }
  
  /**
   * This function makes sure that an exception is always wrapped as a {@link KclException} without
   * unnecessary wrappings.
   * 
   * @param ex   The exception that might need to be wrapped. Not <code>null</code>.
   * 
   * @return   A KclException instance. Not <code>null</code>.
   */
  public static KclException wrap( @NonNull Exception ex ) {
    if( ex instanceof KclException ) {
      return (KclException) ex;
    } else {
      return new KclException( ex.getLocalizedMessage(), ex );
    }
  }
  
  /**
   * This helper function can be used as a generic error handler.
   * 
   * @param ex   The exception that indicates the error. Not <code>null</code>.
   */
  public static void errorHandler( @NonNull Exception ex ) {
    throw wrap(ex);
  }
  
  public static Exception unwrap( @NonNull Exception ex ) {
    Exception result = ex;
    if( (result instanceof KclException) && (result.getCause() != null) ) {
      result = unwrap( (Exception) ((KclException) ex).getCause() );
    }
    return result;
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

  public static <T1, T2> BiConsumer<T1, T2> ensure( BiConsumer<T1, T2> consumer ) {
    return new BiConsumer<T1, T2>() {

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
