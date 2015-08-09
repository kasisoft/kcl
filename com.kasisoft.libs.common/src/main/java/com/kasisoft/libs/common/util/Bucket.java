package com.kasisoft.libs.common.util;

import lombok.experimental.*;

import lombok.*;

import java.util.*;
import java.util.function.*;

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
   * Initializes this bucket for a specific type.
   * 
   * @param type   The type that should be used for the creation. Not <code>null</code>.
   */
  public Bucket( @NonNull Class<T> type ) {
    this( new DefaultBucketFactory<>( type ) );
  }
  
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
  public <P extends T> T allocate() {
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
   * @param object   The object that shall be freed. Not <code>null</code>.
   */
  public void free( @NonNull T object ) {
    synchronized( references ) {
      references.add( new SoftReference<>( factory.reset( object ) ) );
    }
  }

  /**
   * Executes the supplied function with the desired instance.
   * 
   * @param function   The function that is supposed to be executed. Not <code>null</code>.
   * 
   * @return   The return value of the supplied function. Maybe <code>null<code>.
   */
  public <R> R withInstance( @NonNull Function<T,R> function ) {
    T instance = allocate();
    try {
      return function.apply( instance );
    } finally {
      free( instance );
    }
  }

  /**
   * Executes the supplied consumer with the desired instance.
   * 
   * @param consumer   The consumer that is supposed to be executed. Not <code>null</code>.
   */
  public void withInstanceDo( Consumer<T> consumer ) {
    T instance = allocate();
    try {
      consumer.accept( instance );
    } finally {
      free( instance );
    }
  }
  
} /* ENDCLASS */
