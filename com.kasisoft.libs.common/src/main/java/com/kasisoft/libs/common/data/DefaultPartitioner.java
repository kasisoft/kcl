package com.kasisoft.libs.common.data;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

/**
 * Default implementation for Partitioner instances.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultPartitioner<T,S,R extends Collection<S>> implements Partitioner<T,R> {

  Predicate<T>    predicate;
  Function<T,S>   transform;
  R               data;
  
  /**
   * Initializes this default implementation providing a certain collection to store the data.
   * 
   * @param test    The predicate used to determine whether we can select a record or not. Not <code>null</code>.
   * @param model   The model used to collect the data. Not <code>null</code>.
   */
  public DefaultPartitioner( @NonNull Predicate<T> test, @NonNull R model ) {
    this( test, null, model );
  }

  /**
   * Initializes this default implementation providing a certain collection to store the data.
   * 
   * @param test    The predicate used to determine whether we can select a record or not. Not <code>null</code>.
   * @param alter   A transformer which used prepares a record to become collected. 
   *                If <code>null</code> each record will be collected as is.
   * @param model   The model used to collect the data. Not <code>null</code>.
   */
  public DefaultPartitioner( @NonNull Predicate<T> test, Function<T,S> alter, @NonNull R model ) {
    predicate = test;
    data      = model;
    transform = alter;
    if( transform == null ) {
      transform = $ -> (S) $;
    }
  }

  @Override
  public boolean test( T t ) {
    return predicate.test(t);
  }

  @Override
  public R getPartition() {
    if( data instanceof Set ) {
      return (R) Collections.unmodifiableSet( (Set) data );
    } else if( data instanceof List ) {
      return (R) Collections.unmodifiableList( (List) data );
    } else {
      return (R) Collections.unmodifiableCollection( (Collection) data );
    }
  }

  @Override
  public void clear() {
    data.clear();
  }

  @Override
  public void collect( T record ) {
    data.add( transform.apply( record ) );
  }
  
} /* ENDCLASS */
