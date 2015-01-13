package com.kasisoft.libs.common.functionality;

import java.util.*;

import lombok.*;

/**
 * Helper functions to perform operations similar to the functional paradigm.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated Will be removed with version 1.8 which will use Java 8.
 */
@Deprecated
@SuppressWarnings("deprecation")
public class FuFunctions {

  /**
   * Prevent instantiations.
   */
  private FuFunctions() {
  }
  
  /**
   * Reduces a list to a value.
   * 
   * @param function   The function used to perform the reduction. Not <code>null</code>.
   * @param objects    The list which will be reduced. Not <code>null</code>.
   * @param initial    The initial value. Maybe <code>null</code>.
   * 
   * @return   The reduced value. Not <code>null</code>.
   */
  public static <F,T> T reduce( @NonNull Reduce<F,T> function, @NonNull List<F> objects, T initial ) {
    T result = initial;
    for( int i = 0; i < objects.size(); i++ ) {
      result = function.reduce( objects.get(i), result );
    }
    return result;
  }
  
  /**
   * Maps the elements of a list to a new list.
   * 
   * @param function   The function used to perform the transformation. Not <code>null</code>.
   * @param objects    The list which contains the elements to be transformed. Not <code>null</code>.
   * 
   * @return   The transformed values. Not <code>null</code>.
   */
  public static <F,T> List<T> map( @NonNull Transform<F,T> function, @NonNull List<F> objects ) {
    List<T> result = new ArrayList<>();
    for( int i = 0; i < objects.size(); i++ ) {
      result.add( function.map( objects.get(i) ) );
    }
    return result;
  }

  /**
   * Maps one map into another while transforming the value part of each record. 
   * 
   * @param function   The function used to perform the transformation. Not <code>null</code>.
   * @param objects    The map which has to be transformed. Not <code>null</code>.
   * 
   * @return   The transformed map. Not <code>null</code>.
   */
  public static <K,F,T> Map<K,T> mapValue( @NonNull Transform<F,T> function, @NonNull Map<K,F> objects ) {
    return mapValue( function, objects, null );
  }

  /**
   * Maps one map into another while transforming the value part of each record. 
   * 
   * @param function    The function used to perform the transformation. Not <code>null</code>.
   * @param objects     The map which has to be transformed. Not <code>null</code>.
   * @param defvalues   Default values which will be used as a preset. Maybe <code>null</code>.
   * 
   * @return   The transformed map. Not <code>null</code>.
   */
  public static <K,F,T> Map<K,T> mapValue( @NonNull Transform<F,T> function, @NonNull Map<K,F> objects, Map<K,T> defvalues ) {
    Map<K,T> result = new HashMap<>();
    if( defvalues != null ) {
      result.putAll( defvalues );
    }
    for( Map.Entry<K,F> entry : objects.entrySet() ) {
      result.put( entry.getKey(), function.map( entry.getValue() ) );
    }
    return result;
  }

  /**
   * Maps one map into another while transforming the key part of each record. Note that due to the nature of a key the 
   * function needs to be injective. Otherwise two entries might cause an override of a single entry. 
   * 
   * @param function   The function used to perform the transformation. Not <code>null</code>.
   * @param objects    The map which has to be transformed. Not <code>null</code>.
   * 
   * @return   The transformed map. Not <code>null</code>.
   */
  public static <V,F,T> Map<T,V> mapKey( @NonNull Transform<F,T> function, @NonNull Map<F,V> objects ) {
    Map<T,V> result = new HashMap<>();
    for( Map.Entry<F,V> entry : objects.entrySet() ) {
      result.put( function.map( entry.getKey() ), entry.getValue() );
    }
    return result;
  }

  /**
   * Zips two lists while recombining their elements and recreating a new list. 
   * 
   * @param function   The function used to perform the recombination. Not <code>null</code>.
   * @param left       The list which contains the elements of the left side. Not <code>null</code>.
   * @param right      The list which contains the elements of the right side. Not <code>null</code>.
   * 
   * @return   The list with the recombined values. Not <code>null</code>.
   */
  public static <L,R,V> List<V> zipEqualLengths( @NonNull Zip<L,R,V> function, @NonNull List<L> left, @NonNull List<R> right ) {
    return zip( function, left, right );
  }
  
  /**
   * Zips two lists while recombining their elements and recreating a new list. It is allowed to have lists with 
   * different lengths. Missing elements will be passed as <code>null</code> values to the function.
   * 
   * @param function    The function used to perform the recombination. Not <code>null</code>.
   * @param left        The list which contains the elements of the left side. Not <code>null</code>.
   * @param right       The list which contains the elements of the right side. Not <code>null</code>.
   * 
   * @return   The list with the recombined values. Not <code>null</code>.
   */
  public static <L,R,V> List<V> zip( @NonNull Zip<L,R,V> function, @NonNull List<L> left, @NonNull List<R> right ) {
    int     max    = Math.max( left.size(), right.size() );
    List<V> result = new ArrayList<>();
    for( int i = 0; i < max; i++ ) {
      L leftobj  = null;
      R rightobj = null;
      if( i < left.size() ) {
        leftobj = left.get(i);
      }
      if( i < right.size() ) {
        rightobj = right.get(i);
      }
      result.add( function.zip( leftobj, rightobj ) );
    }
    return result;
  }

  /**
   * Creates a new list which guarantees that each element is accepted by the supplied filter.
   *  
   * @param function   The function which provides the filter. Not <code>null</code>.
   * @param objects    The objects that have to be tested. Not <code>null</code>.
   * 
   * @return   A newly created list. Not <code>null</code>.
   */
  public static <T> List<T> filter( @NonNull Filter<T> function, @NonNull List<T> objects ) {
    List<T> result = new ArrayList<>();
    for( int i = 0; i < objects.size(); i++ ) {
      T object = objects.get(i);
      if( function.accept( object ) ) {
        result.add( object );
      }
    }
    return result;
  }
  
} /* ENDCLASS */
