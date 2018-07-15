package com.kasisoft.libs.common.function;

import java.util.function.*;

import lombok.*;

/**
 * Collection of useful functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Functions {

  private Functions() {
  }
  
  /**
   * Protects the supplied function while testing for a null input.
   *  
   * @param delegate   The original function. Not <code>null</code>.
   * 
   * @return   The <code>null</code> aware function. <code>null</code> values will be passed through.
   *           Not <code>null</code>.
   */
  public static <T> Function<T, T> nullSafe( @NonNull Function<T, T> delegate ) {
    return $ -> $ != null ? delegate.apply($) : null;
  }
  
  public static <A, B, C, R> TriFunction<A, B, C, R> adapt( Function<A, R> function ) {
    return ($a, $b, $c) -> function.apply($a);
  }

  public static <A, B, C, R> TriFunction<A, B, C, R> adapt( BiFunction<A, B, R> function ) {
    return ($a, $b, $c) -> function.apply($a, $b);
  }

  public static <A, B, C> TriConsumer<A, B, C> adapt( Consumer<A> consumer ) {
    return ($a, $b, $c) -> consumer.accept($a);
  }

  public static <A, B, C> TriConsumer<A, B, C> adapt( BiConsumer<A, B> consumer ) {
    return ($a, $b, $c) -> consumer.accept($a, $b);
  }

  public static <E extends Exception> void errorHandling( E exception, Consumer<E> errHandler, Consumer<E> defaultErrHandler ) {
    if( errHandler != null ) {
      errHandler.accept( exception );
    } else if( defaultErrHandler != null ) {
      defaultErrHandler.accept( exception );
    }
  }
  
  public static <E extends Exception, C1> void errorHandling( E exception, C1 arg1, BiConsumer<E, C1> errHandler, BiConsumer<E, C1> defaultErrHandler ) {
    if( errHandler != null ) {
      errHandler.accept( exception, arg1 );
    } else if( defaultErrHandler != null ) {
      defaultErrHandler.accept( exception, arg1 );
    }
  }

  public static <E extends Exception, C1, C2> void errorHandling( E exception, C1 arg1, C2 arg2, TriConsumer<E, C1, C2> errHandler, TriConsumer<E, C1, C2> defaultErrHandler ) {
    if( errHandler != null ) {
      errHandler.accept( exception, arg1, arg2 );
    } else if( defaultErrHandler != null ) {
      defaultErrHandler.accept( exception, arg1, arg2 );
    }
  }
  
} /* ENDCLASS */
