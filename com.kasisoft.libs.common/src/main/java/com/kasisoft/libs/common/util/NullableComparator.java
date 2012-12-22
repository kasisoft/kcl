/**
 * Name........: NullableComparator
 * Description.: A Comparator implementation that is capable to handle <code>null</code> values.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import java.util.*;

/**
 * A Comparator implementation that is capable to handle <code>null</code> values. The default implementation which
 * doesn't use a {@link Comparator} expects the generic type <code>T</code> to be an extension of {@link Comparable}.
 * Otherwise this implementation will cause a {@link ClassCastException} while being used.
 */
public class NullableComparator<T> implements Comparator<T> {

  private Comparator<T>   delegate;
  
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

  /**
   * {@inheritDoc}
   */
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
