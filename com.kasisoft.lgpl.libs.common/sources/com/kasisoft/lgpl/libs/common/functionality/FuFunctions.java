/**
 * Name........: FuFunctions
 * Description.: Helper functions to perform operations similar to the functional paradigm.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.functionality;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.*;

/**
 * Helper functions to perform operations similar to the functional paradigm.
 */
@KDiagnostic
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
   * @param initial    The initial value. <code>null</code>.
   * 
   * @return   The reduced value. Not <code>null</code>.
   */
  public static final <F,T> T reduce( 
    @KNotNull(name="function")   Reduce<F,T>   function, 
    @KNotNull(name="objects")    List<F>       objects, 
    @KNotNull(name="initial")    T             initial 
  ) {
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
  public static final <F,T> List<T> map( 
    @KNotNull(name="function")   Transform<F,T>   function, 
    @KNotNull(name="objects")    List<F>          objects 
  ) {
    List<T> result = new ArrayList<T>();
    for( int i = 0; i < objects.size(); i++ ) {
      result.add( function.map( objects.get(i) ) );
    }
    return result;
  }

  /**
   * Zips two lists while recombining their elements and recreating a new list. It is <b>not</b>
   * allowed to have lists with different lengths. 
   * 
   * @param function   The function used to perform the recombination. Not <code>null</code>.
   * @param left       The list which contains the elements of the left side. Not <code>null</code>.
   * @param right      The list which contains the elements of the right side. Not <code>null</code>.
   * 
   * @return   The list with the recombined values. Not <code>null</code>.
   */
  public static final <L,R,V> List<V> zipE( 
                              Zip<L,R,V>   function, 
    @KNotNull(name="left")    List<L>      left, 
    @KNotNull(name="right")   List<R>      right 
  ) {
    if( left.size() != right.size() ) {
      throw new IllegalArgumentException( "the parameters 'left' and 'right' must have the same sizes. currently they are " + left.size() + " and " + right.size() );
    }
    return zip( function, left, right );
  }
  
  /**
   * Zips two lists while recombining their elements and recreating a new list. It is allowed
   * to have lists with different lengths. Missing elements will be passed as <code>null</code>
   * values to the function.
   * 
   * @param function    The function used to perform the recombination. Not <code>null</code>.
   * @param left        The list which contains the elements of the left side. Not <code>null</code>.
   * @param right       The list which contains the elements of the right side. Not <code>null</code>.
   * 
   * @return   The list with the recombined values. Not <code>null</code>.
   */
  public static final <L,R,V> List<V> zip( 
    @KNotNull(name="function")   Zip<L,R,V>   function, 
    @KNotNull(name="left")       List<L>      left, 
    @KNotNull(name="right")      List<R>      right 
  ) {
    int     max    = Math.max( left.size(), right.size() );
    List<V> result = new ArrayList<V>();
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
  public static final <T> List<T> filter(
    @KNotNull(name="function")   Filter<T>   function, 
    @KNotNull(name="objects")    List<T>     objects 
  ) {
    List<T> result = new ArrayList<T>();
    for( int i = 0; i < objects.size(); i++ ) {
      T object = objects.get(i);
      if( function.accept( object ) ) {
        result.add( object );
      }
    }
    return result;
  }
  
} /* ENDCLASS */
