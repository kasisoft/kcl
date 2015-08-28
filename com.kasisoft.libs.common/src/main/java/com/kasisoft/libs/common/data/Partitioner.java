package com.kasisoft.libs.common.data;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface Partitioner<T,R> extends Predicate<T> {

  /**
   * Create a new partition which allows to collect some data.
   * 
   * @return   The new partition instance used to collect some data. Not <code>null</code>.
   */
  R newPartition();
  
  /**
   * Collects a certain record while adding it to the supplied partition. This only applies to records successfully
   * tested by this predicate.
   *
   * @param partition  The partition that will be expanded. Not <code>null</code>.
   * @param record     The record that will be added. Not <code>null</code>.
   */
  void collect( R partition, T record );
  
} /* ENDINTERFACE */
