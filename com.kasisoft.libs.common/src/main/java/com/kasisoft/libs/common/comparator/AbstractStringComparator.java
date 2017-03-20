package com.kasisoft.libs.common.comparator;

import java.util.function.*;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Abstract comparator.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
abstract class AbstractStringComparator<T> implements Comparator<T> {

  BiFunction<String, String, Integer>   comparison;
  
  public AbstractStringComparator( boolean ignoreCase ) {
    comparison = ignoreCase ? this::compareToIgnoreCase : this::compareTo;
  }
  
  protected int compare( String s1, String s2 ) {
    return comparison.apply( s1, s2 );
  }
  
  private int compareToIgnoreCase( String s1, String s2 ) {
    return s1.compareToIgnoreCase( s2 );
  }
  
  private int compareTo( String s1, String s2 ) {
    return s1.compareTo( s2 );
  }

} /* ENDCLASS */
