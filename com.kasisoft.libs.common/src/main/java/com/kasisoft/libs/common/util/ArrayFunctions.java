/**
 * Name........: ArrayFunctions
 * Description.: Collection of functions useful in conjunction with arrays. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import java.util.*;

/**
 * Collection of functions useful in conjunction with arrays.
 */
public class ArrayFunctions {

  /**
   * Prevent instantiation.
   */
  private ArrayFunctions() {
  }
  
  /**
   * Returns the maximum of the supplied values.
   * 
   * @param args   The values used to determine the maximum. Must have at least the length 1.
   * 
   * @return   The maximum of all values.
   */
  public static int maxInt( int ... args ) {
    if( args.length == 1 ) {
      return args[0];
    }
    int result = Math.max( args[0], args[1] );
    for( int i = 2; i < args.length; i++ ) {
      result = Math.max( args[i], result );
    }
    return result;
  }

  /**
   * Returns the maximum of the supplied values.
   * 
   * @param args   The values used to determine the maximum. Must have at least the length 1.
   * 
   * @return   The maximum of all values.
   */
  public static long maxLong( long ... args ) {
    if( args.length == 1 ) {
      return args[0];
    }
    long result = Math.max( args[0], args[1] );
    for( int i = 2; i < args.length; i++ ) {
      result = Math.max( args[i], result );
    }
    return result;
  }

  /**
   * Returns the maximum of the supplied values.
   * 
   * @param args   The values used to determine the maximum. Must have at least the length 1.
   * 
   * @return   The maximum of all values.
   */
  public static float maxFloat( float ... args ) {
    if( args.length == 1 ) {
      return args[0];
    }
    float result = Math.max( args[0], args[1] );
    for( int i = 2; i < args.length; i++ ) {
      result = Math.max( args[i], result );
    }
    return result;
  }

  /**
   * Returns the maximum of the supplied values.
   * 
   * @param args   The values used to determine the maximum. Must have at least the length 1.
   * 
   * @return   The maximum of all values.
   */
  public static double maxDouble( double ... args ) {
    if( args.length == 1 ) {
      return args[0];
    }
    double result = Math.max( args[0], args[1] );
    for( int i = 2; i < args.length; i++ ) {
      result = Math.max( args[i], result );
    }
    return result;
  }

  /**
   * Returns the minimum of the supplied values.
   * 
   * @param args   The values used to determine the minimum. Must have at least the length 1.
   * 
   * @return   The minimum of all values.
   */
  public static int minInt( int ... args ) {
    if( args.length == 1 ) {
      return args[0];
    }
    int result = Math.min( args[0], args[1] );
    for( int i = 2; i < args.length; i++ ) {
      result = Math.min( args[i], result );
    }
    return result;
  }

  /**
   * Returns the minimum of the supplied values.
   * 
   * @param args   The values used to determine the minimum. Must have at least the length 1.
   * 
   * @return   The minimum of all values.
   */
  public static long minLong( long ... args ) {
    if( args.length == 1 ) {
      return args[0];
    }
    long result = Math.min( args[0], args[1] );
    for( int i = 2; i < args.length; i++ ) {
      result = Math.min( args[i], result );
    }
    return result;
  }

  /**
   * Returns the minimum of the supplied values.
   * 
   * @param args   The values used to determine the minimum. Must have at least the length 1.
   * 
   * @return   The minimum of all values.
   */
  public static float minFloat( float ... args ) {
    if( args.length == 1 ) {
      return args[0];
    }
    float result = Math.min( args[0], args[1] );
    for( int i = 2; i < args.length; i++ ) {
      result = Math.min( args[i], result );
    }
    return result;
  }

  /**
   * Returns the minimum of the supplied values.
   * 
   * @param args   The values used to determine the minimum. Must have at least the length 1.
   * 
   * @return   The minimum of all values.
   */
  public static double minDouble( double ... args ) {
    if( args.length == 1 ) {
      return args[0];
    }
    double result = Math.min( args[0], args[1] );
    for( int i = 2; i < args.length; i++ ) {
      result = Math.min( args[i], result );
    }
    return result;
  }
  
  /**
   * Runs the AND operation for the supplied values.
   * 
   * @param atoms   Atomic expressions. Not <code>null</code>.
   * 
   * @return   The boolean result. Not <code>null</code>.
   */
  public static Boolean objectAnd( Boolean ... atoms ) {
    boolean result = atoms[0].booleanValue();
    for( int i = 0; (i < atoms.length) && result; i++ ) {
      result = result && atoms[i].booleanValue();
    }
    return Boolean.valueOf( result );
  }

  /**
   * Runs the OR operation for the supplied values.
   * 
   * @param atoms   Atomic expressions. Not <code>null</code>.
   * 
   * @return   The boolean result. Not <code>null</code>.
   */
  public static Boolean objectOr( Boolean ... atoms ) {
    boolean result = atoms[0].booleanValue();
    for( int i = 0; (i < atoms.length) && (! result); i++ ) {
      result = result || atoms[i].booleanValue();
    }
    return Boolean.valueOf( result );
  }

  /**
   * Determines the and'ed result of the supplied arguments.
   * 
   * @param args   A list of booleans to be combined. Must have at least the length 1.
   * 
   * @return   <code>true</code> <=> Each argument was <code>true</code>.
   */
  public static boolean and( boolean ... args ) {
    boolean result = args[0];
    for( int i = 1; (i < args.length) && result; i++ ) {
      result = result && args[i];
    }
    return result;
  }

  /**
   * Determines the or'ed result of the supplied arguments.
   * 
   * @param args   A list of booleans to be combined. Must have at least the length 1.
   * 
   * @return   <code>true</code> <=> At least one argument was <code>true</code>.
   */
  public static boolean or( boolean ... args ) {
    boolean result = args[0];
    for( int i = 1; (i < args.length) && (! result); i++ ) {
      result = result || args[i];
    }
    return result;
  }

  /**
   * Extendss a list with an array and returns it. The order is preserved. The list will be extended so any previously 
   * existing content remains there.
   * 
   * @param receiver   The list which will be extended. Not <code>null</code>.
   * @param input      The data items which have to be added. Maybe <code>null</code>.
   * 
   * @return   The list that has been supplied. Not <code>null</code>.
   */
  public static <T> List<T> addAll( List<T> receiver, T ... input ) {
    if( input != null ) {
      for( T object : input ) {
        receiver.add( object );
      }
    }
    return receiver;
  }
  
  /**
   * Returns an Enumeration which is usable to iterate through the supplied array.
   * 
   * @param input   The array which should be traversed.
   * 
   * @return   The enumeration which is used to traverse the array.
   */
  public static <T> Enumeration<T> enumeration( T ... input ) {
    return new ArrayTraversal<T>( input );
  }

  /**
   * Returns an Iterator which is usable to iterate through the supplied array.
   * 
   * @param input   The array which should be traversed.
   * 
   * @return   The Iterator which is used to traverse the array.
   */
  public static <T> Iterator<T> iterator( T ... input ) {
    return new ArrayTraversal<T>( input );
  }

  /**
   * Adds all array entries together.
   *
   * @param values  Array of numbers. Maybe <code>null</code>.
   *
   * @return  Sum of these numbers.
   */
  public static int sum( int ... values ) {
    int result = 0;
    if( (values != null) && (values.length > 0) ) {
      for( int i = 0; i < values.length; i++ ) {
        if( values[i] > 0 ) {
          result += values[i];
        }
      }
    }
    return result;
  }

  /**
   * Adds all array entries together.
   *
   * @param values  Array of numbers. Maybe <code>null</code>.
   *
   * @return  Sum of these numbers.
   */
  public static double sum( double ... values ) {
    double result = 0;
    if( (values != null) && (values.length > 0) ) {
      for( int i = 0; i < values.length; i++ ) {
        if( values[i] > 0 ) {
          result += values[i];
        }
      }
    }
    return result;
  }

  /**
   * Adds all array entries together.
   *
   * @param values  Array of numbers. Maybe <code>null</code>.
   *
   * @return  Sum of these numbers.
   */
  public static long sum( long ... values ) {
    long result = 0;
    if( (values != null) && (values.length > 0) ) {
      for( int i = 0; i < values.length; i++ ) {
        if( values[i] > 0 ) {
          result += values[i];
        }
      }
    }
    return result;
  }

  /**
   * Implementation of the interfaces Enumeration and Iterator allowing to traverse arrays.
   */
  private static class ArrayTraversal<T> implements Enumeration<T>, Iterator<T> {
    
    private T[]   data;
    private int   index;
    
    /**
     * Initialises this traversal class.
     * 
     * @param input   The array which can be traversed.
     */
    public ArrayTraversal( T[] input ) {
      data  = input;
      index = 0;
    }

    @Override
    public boolean hasMoreElements() {
      return index < data.length;
    }

    @Override
    public boolean hasNext() {
      return hasMoreElements();
    }

    @Override
    public T nextElement() {
      if( index >= data.length ) {
        throw new NoSuchElementException();
      }
      T result = data[ index ];
      index++;
      return result;
    }

    @Override
    public T next() {
      return nextElement();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
