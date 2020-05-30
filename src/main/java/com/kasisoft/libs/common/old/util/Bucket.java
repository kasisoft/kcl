package com.kasisoft.libs.common.old.util;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

import java.lang.ref.*;
 
/**
 * Collector for often used objects like collections, maps etc. .
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bucket<T> {

  List<SoftReference<T>>   references;
  BucketFactory<T>         factory;

  /**
   * Initializes this bucket.
   * 
   * @param bucketfactory   The factory that will be used to create/reset new objects. Not <code>null</code>.
   */
  public Bucket( @NonNull BucketFactory<T> bucketfactory ) {
    references  = new LinkedList<>();
    factory     = bucketfactory;
  }
  
  /**
   * Returns the number of references currently stored.
   * 
   * @return   The number of references currently stored.
   */
  public int getSize() {
    return references.size();
  }
  
  /**
   * Creates a new object (potentially a reused one).
   * 
   * @return   A new object.
   */
  public T allocate() {
    T result = null;
    synchronized( references ) {
      while( (result == null) && (! references.isEmpty()) ) {
        SoftReference<T> reference = references.remove(0);
        result                     = reference.get();
        reference.clear();
      }
    }
    if( result == null ) {
      result = factory.create();
    }
    return result;
  }

  /**
   * Frees the supplied object, so it's allowed to be reused.
   * 
   * @param object   The object that shall be freed. Maybe <code>null</code>.
   */
  public <R extends T> void free( R object ) {
    if( object != null ) {
      synchronized( references ) {
        references.add( new SoftReference<>( factory.reset( object ) ) );
      }
    }
  }

  /**
   * Executes the supplied function with the desired instance.
   * 
   * @param function   The function that is supposed to be executed. Not <code>null</code>.
   * 
   * @return   The return value of the supplied function. Maybe <code>null<code>.
   */
  public <R> R forInstance( @NonNull Function<T, R> function ) {
    T instance = allocate();
    try {
      return function.apply( instance );
    } finally {
      free( instance );
    }
  }

  /**
   * Executes the supplied function with the desired instance.
   * 
   * @param function   The function that is supposed to be executed. Not <code>null</code>.
   * @param param      An additional parameter for the function.
   * 
   * @return   The return value of the supplied function. Maybe <code>null<code>.
   */
  public <R, P> R forInstance( @NonNull BiFunction<T, P, R> function, P param ) {
    T instance = allocate();
    try {
      return function.apply( instance, param );
    } finally {
      free( instance );
    }
  }

  /**
   * Executes the supplied consumer with the desired instance.
   * 
   * @param consumer   The consumer that is supposed to be executed. Not <code>null</code>.
   */
  public void forInstanceDo( Consumer<T> consumer ) {
    T instance = allocate();
    try {
      consumer.accept( instance );
    } finally {
      free( instance );
    }
  }

  /**
   * Executes the supplied consumer with the desired instance.
   * 
   * @param consumer   The consumer that is supposed to be executed. Not <code>null</code>.
   * @param param      An additional parameter for the function.
   */
  public <P> void forInstanceDo( BiConsumer<T, P> consumer, P param ) {
    T instance = allocate();
    try {
      consumer.accept( instance, param );
    } finally {
      free( instance );
    }
  }

} /* ENDCLASS */
