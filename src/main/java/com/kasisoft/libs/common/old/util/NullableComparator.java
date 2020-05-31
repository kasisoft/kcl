package com.kasisoft.libs.common.old.util;

import java.util.Comparator;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * A Comparator implementation that is capable to handle <code>null</code> values. The default implementation which
 * doesn't use a {@link Comparator} expects the generic type <code>T</code> to be an extension of {@link Comparable}.
 * Otherwise this implementation will cause a {@link ClassCastException} while being used.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NullableComparator<T> implements Comparator<T> {

  Comparator<T>   delegate;
  
  /**
   * Initialises this Comparator instance totally relying on the Comparable implementation.
   */
  public NullableComparator() {
    delegate = null;
  }

  /**
   * Initialises this Comparator instance relying on the supplied Comparator.
   * 
   * @param impl   A Comparator implementation used to perform the comparison for two values. Maybe <code>null</code>.
   */
  public NullableComparator( Comparator<T> impl ) {
    delegate = impl;
  }

  @Override
  public int compare( T o1, T o2 ) {
    if( (o1 == null) && (o2 == null) ) {
      return 0;
    }
    if( (o1 != null) && (o2 != null) ) {
      if( delegate != null ) {
        return delegate.compare( o1, o2 );
      } else {
        return ((Comparable<T>) o1).compareTo( o2 );
      }
    }
    if( o1 != null ) {
      return 1;
    } else /* if( o2 != null ) */ {
      return -1;
    }
  }
  
} /* ENDCLASS */
