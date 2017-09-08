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
public class DefaultPartitioner<T, K, S, R extends Collection<S>> implements Partitioner<T, K, R> {

  Predicate<T>          predicate;
  BiFunction<K, T, S>   transform;
  R                     data;
  
  /**
   * Initializes this default implementation providing a certain collection to store the data.
   * 
   * @param test    The predicate used to determine whether we can select a record or not. Not <code>null</code>.
   * @param model   The model used to collect the data. Not <code>null</code>.
   */
  public DefaultPartitioner( Predicate<T> test, R model ) {
    this( test, (BiFunction<K, T, S>) null, model );
  }

  /**
   * Initializes this default implementation providing a certain collection to store the data.
   * 
   * @param test    The predicate used to determine whether we can select a record or not. Not <code>null</code>.
   * @param alter   A transformer which prepares a record to become collected. 
   *                If <code>null</code> each record will be collected as is.
   * @param model   The model used to collect the data. Not <code>null</code>.
   */
  public DefaultPartitioner( Predicate<T> test, Function<T, S> alter, R model ) {
    this( test, ($, v) -> alter.apply(v), model );
  }
  
  /**
   * Initializes this default implementation providing a certain collection to store the data.
   * 
   * @param test    The predicate used to determine whether we can select a record or not. Not <code>null</code>.
   * @param alter   A transformer which prepares a record to become collected. 
   *                If <code>null</code> each record will be collected as is.
   * @param model   The model used to collect the data. Not <code>null</code>.
   */
  public DefaultPartitioner( @NonNull Predicate<T> test, BiFunction<K, T, S> alter, @NonNull R model ) {
    predicate = test;
    data      = model;
    transform = alter;
    if( transform == null ) {
      transform = ($1, $2) -> (S) $2;
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
  public void collect( @NonNull K key, T record ) {
    data.add( transform.apply( key, record ) );
  }
  
} /* ENDCLASS */
