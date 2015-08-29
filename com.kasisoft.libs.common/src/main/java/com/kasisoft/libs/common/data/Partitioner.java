package com.kasisoft.libs.common.data;

import java.util.function.*;

/**
 * Each implementor allows to collect data.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface Partitioner<T,K,R> extends Predicate<T> {

  /**
   * Returns the data instance associated with this partitioner.
   *  
   * @return   The data instance associated with this partitioner. Not <code>null</code>.
   */
  R getPartition();

  /**
   * Allow to clear the inner state if necessary.
   */
  void clear();

  /**
   * Collects a certain record while adding it to this partition. This only applies to records successfully
   * tested by this predicate.
   *
   * @param key      The key used to access a certain partition element. Not <code>null</code>.
   * @param record   The record that will be added. Not <code>null</code>.
   */
  void collect( K key, T record );
  
} /* ENDINTERFACE */
