package com.kasisoft.libs.common.util;

import java.util.function.*;

/**
 * Factory implementation used to fill up the Bucket.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface BucketFactory<T> {

  /**
   * Creates a new instance.
   * 
   * @return   A new instance. Not <code>null</code>.
   */
  <P extends T> P create();

  /**
   * Resets the supplied object.
   * 
   * @param object   The object that is supposed to be cleared. Not <code>null</code>.
   *          
   * @return   The supplied object. Not <code>null</code>.
   */
  <P extends T> P reset( T object );
  
  /**
   * Executes the supplied function with the desired instance.
   * 
   * @param function   The function that is supposed to be executed. Not <code>null</code>.
   * 
   * @return   The return value of the supplied function. Maybe <code>null<code>.
   */
  default <R> R forInstance( Function<T, R> function ) {
    T instance = create();
    try {
      return function.apply( instance );
    } finally {
      reset( instance );
    }
  }

  /**
   * Executes the supplied function with the desired instance.
   * 
   * @param function      The function that is supposed to be executed. Not <code>null</code>.
   * @param secondParam   The second parameter.
   * 
   * @return   The return value of the supplied function. Maybe <code>null<code>.
   */
  default <R, O> R forInstance( BiFunction<T, O, R> function, O secondParam ) {
    T instance = create();
    try {
      return function.apply( instance, secondParam );
    } finally {
      reset( instance );
    }
  }

  /**
   * Executes the supplied consumer with the desired instance.
   * 
   * @param consumer   The consumer that is supposed to be executed. Not <code>null</code>.
   */
  default void forInstanceDo( Consumer<T> consumer ) {
    T instance = create();
    try {
      consumer.accept( instance );
    } finally {
      reset( instance );
    }
  }

  /**
   * Executes the supplied consumer with the desired instance.
   * 
   * @param consumer      The consumer that is supposed to be executed. Not <code>null</code>.
   * @param secondParam   The second parameter.
   */
  default <O> void forInstanceDo( BiConsumer<T, O> consumer, O secondParam ) {
    T instance = create();
    try {
      consumer.accept( instance, secondParam );
    } finally {
      reset( instance );
    }
  }

} /* ENDINTERFACE */
