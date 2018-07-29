package com.kasisoft.libs.common.function;

import java.util.function.*;

import java.awt.event.*;

import lombok.*;

/**
 * Collection of useful functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Functions {

  private Functions() {
  }
  
  public static ActionListener adaptToActionListener( Consumer<ActionEvent> handler ) {
    return new ActionListener() {
      @Override
      public void actionPerformed( ActionEvent evt ) {
        handler.accept(evt);
      }
    };
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
  
  public static <A, B, C, R> TriFunction<A, B, C, R> adaptToTri( Function<A, R> function ) {
    return ($a, $b, $c) -> function.apply($a);
  }

  public static <A, B, C, R> TriFunction<A, B, C, R> adaptToTri( BiFunction<A, B, R> function ) {
    return ($a, $b, $c) -> function.apply($a, $b);
  }

  public static <A, B, C> TriConsumer<A, B, C> adaptToTri( Consumer<A> consumer ) {
    return ($a, $b, $c) -> consumer.accept($a);
  }

  public static <A, B, C> TriConsumer<A, B, C> adaptToTri( BiConsumer<A, B> consumer ) {
    return ($a, $b, $c) -> consumer.accept($a, $b);
  }

  public static <A, B, R> BiFunction<A, B, R> adaptToBi( Function<A, R> function ) {
    return ($a, $b) -> function.apply($a);
  }

  public static <A, B> BiConsumer<A, B> adaptToBi( Consumer<A> consumer ) {
    return ($a, $b) -> consumer.accept($a);
  }

  public static <A, B, R> BiFunction<A, B, R> adaptToBi( TriFunction<A, B, ?, R> function ) {
    return ($a, $b) -> function.apply($a, $b, null);
  }

  public static <A, B> BiConsumer<A, B> adaptToBi( TriConsumer<A, B, ?> consumer ) {
    return ($a, $b) -> consumer.accept($a, $b, null);
  }
  
  public static <A, R> Function<A, R> adapt( BiFunction<A, ?, R> function ) {
    return $ -> function.apply($, null);
  }

  public static <A, B> Consumer<A> adapt( BiConsumer<A, ?> consumer ) {
    return $ -> consumer.accept($, null);
  }

  public static <A, R> Function<A, R> adapt( TriFunction<A, ?, ?, R> function ) {
    return $ -> function.apply($, null, null);
  }

  public static <A> Consumer<A> adapt( TriConsumer<A, ?, ?> consumer ) {
    return $ -> consumer.accept($, null, null);
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
