/**
 * Name........: ArrayFunctions
 * Description.: Collection of functions useful in conjunction with arrays. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

import lombok.*;

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
   * This method casts the supplied primitives into the corresponding object types.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Boolean[] toObjectArray( boolean[] values ) {
    if( values == null ) {
      return null;
    } else {
      Boolean[] result = new Boolean[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = Boolean.valueOf( values[i] );
      }
      return result;
    }
  }

  /**
   * This method casts the supplied primitives into the corresponding object types.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Byte[] toObjectArray( byte[] values ) {
    if( values == null ) {
      return null;
    } else {
      Byte[] result = new Byte[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = Byte.valueOf( values[i] );
      }
      return result;
    }
  }
  
  /**
   * This method casts the supplied primitives into the corresponding object types.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Character[] toObjectArray( char[] values ) {
    if( values == null ) {
      return null;
    } else {
      Character[] result = new Character[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = Character.valueOf( values[i] );
      }
      return result;
    }
  }

  /**
   * This method casts the supplied primitives into the corresponding object types.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Short[] toObjectArray( short[] values ) {
    if( values == null ) {
      return null;
    } else {
      Short[] result = new Short[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = Short.valueOf( values[i] );
      }
      return result;
    }
  }

  /**
   * This method casts the supplied primitives into the corresponding object types.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Integer[] toObjectArray( int[] values ) {
    if( values == null ) {
      return null;
    } else {
      Integer[] result = new Integer[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = Integer.valueOf( values[i] );
      }
      return result;
    }
  }

  /**
   * This method casts the supplied primitives into the corresponding object types.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Long[] toObjectArray( long[] values ) {
    if( values == null ) {
      return null;
    } else {
      Long[] result = new Long[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = Long.valueOf( values[i] );
      }
      return result;
    }
  }

  /**
   * This method casts the supplied primitives into the corresponding object types.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Float[] toObjectArray( float[] values ) {
    if( values == null ) {
      return null;
    } else {
      Float[] result = new Float[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = Float.valueOf( values[i] );
      }
      return result;
    }
  }

  /**
   * This method casts the supplied primitives into the corresponding object types.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Double[] toObjectArray( double[] values ) {
    if( values == null ) {
      return null;
    } else {
      Double[] result = new Double[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = Double.valueOf( values[i] );
      }
      return result;
    }
  }

//  PBoolean  ( Boolean   . TYPE , Boolean   . class , boolean [] . class , Boolean   [] . class, 0                   , 0                   ) ,
//  PByte     ( Byte      . TYPE , Byte      . class , byte    [] . class , Byte      [] . class, Byte    . MIN_VALUE , Byte    . MAX_VALUE ) ,
//  PChar     ( Character . TYPE , Character . class , char    [] . class , Character [] . class, 0                   , 0                   ) ,
//  PShort    ( Short     . TYPE , Short     . class , short   [] . class , Short     [] . class, Short   . MIN_VALUE , Short   . MAX_VALUE ) ,
//  PInt      ( Integer   . TYPE , Integer   . class , int     [] . class , Integer   [] . class, Integer . MIN_VALUE , Integer . MAX_VALUE ) ,
//  PLong     ( Long      . TYPE , Long      . class , long    [] . class , Long      [] . class, Long    . MIN_VALUE , Long    . MAX_VALUE ) ,
//  PFloat    ( Float     . TYPE , Float     . class , float   [] . class , Float     [] . class, 0                   , 0                   ) ,
//  PDouble   ( Double    . TYPE , Double    . class , double  [] . class , Double    [] . class, 0                   , 0                   ) ;
  
  /**
   * Counts the number of non-<code>null</code> values within the supplied array.
   * 
   * @param objects   The array which values have to be investigated. Maybe <code>null</code>.
   * 
   * @return   The number of non-<code>null</code> values.
   */
  public static <T> int nonNullLength( T ... objects ) {
    int result = 0;
    if( objects != null ) {
      for( int i = 0; i < objects.length; i++ ) {
        if( objects[i] != null ) {
          result++;
        }
      }
    }
    return result;
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Not <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Boolean[] cleanup( @NonNull Boolean ... values ) {
    return cleanup( values, Primitive.PBoolean );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Not <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Character[] cleanup( @NonNull Character ... values ) {
    return cleanup( values, Primitive.PChar );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Not <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Byte[] cleanup( @NonNull Byte ... values ) {
    return cleanup( values, Primitive.PByte );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Not <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Short[] cleanup( @NonNull Short ... values ) {
    return cleanup( values, Primitive.PShort );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Not <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Integer[] cleanup( @NonNull Integer ... values ) {
    return cleanup( values, Primitive.PInt );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Not <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Long[] cleanup( @NonNull Long ... values ) {
    return cleanup( values, Primitive.PLong );
  }
  
  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Not <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Float[] cleanup( @NonNull Float ... values ) {
    return cleanup( values, Primitive.PFloat );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Not <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Double[] cleanup( @NonNull Double ... values ) {
    return cleanup( values, Primitive.PDouble );
  }

  /**
   * A simple helper which removes all <code>null</code> entries from an array.
   * 
   * @param input       The input array providing all values. Maybe <code>null</code>.
   * @param primitive   The object type. Not <code>null</code>.
   * 
   * @return   A cleaned up array or <code>null</code>.
   */
  private static <T> T[] cleanup( T[] input, Primitive primitive ) {
    int count = nonNullLength( input );
    if( count == 0 ) {
      return null;
    } else if( count == input.length ) {
      return input;
    } else {
      T[] result = primitive.newObjectArray( count );
      for( int i = 0, j = 0; i < input.length; i++ ) {
        if( input[i] != null ) {
          result[j++] = input[i];
        }
      }
      return result;
    }
  }

  /**
   * Returns the maximum of the supplied values.
   * 
   * @param args   The values used to determine the maximum. Must have at least the length 1.
   * 
   * @return   The maximum of all values.
   */
  public static int maxInt( @NonNull int ... args ) {
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
  public static long maxLong( @NonNull long ... args ) {
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
  public static float maxFloat( @NonNull float ... args ) {
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
  public static double maxDouble( @NonNull double ... args ) {
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
  public static int minInt( @NonNull int ... args ) {
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
  public static long minLong( @NonNull long ... args ) {
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
  public static float minFloat( @NonNull float ... args ) {
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
  public static double minDouble( @NonNull double ... args ) {
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
  public static Boolean objectAnd( @NonNull Boolean ... atoms ) {
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
  public static Boolean objectOr( @NonNull Boolean ... atoms ) {
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
  public static boolean and( @NonNull boolean ... args ) {
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
  public static boolean or( @NonNull boolean ... args ) {
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
  public static <T> List<T> addAll( @NonNull List<T> receiver, T ... input ) {
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
   * @param input   The array which should be traversed. Not <code>null</code>.
   * 
   * @return   The enumeration which is used to traverse the array.
   */
  public static <T> Enumeration<T> enumeration( @NonNull T ... input ) {
    return new ArrayTraversal<T>( input );
  }

  /**
   * Returns an Iterator which is usable to iterate through the supplied array.
   * 
   * @param input   The array which should be traversed. Not <code>null</code>.
   * 
   * @return   The Iterator which is used to traverse the array.
   */
  public static <T> Iterator<T> iterator( @NonNull T ... input ) {
    return new ArrayTraversal<T>( input );
  }

  /**
   * Adds all array entries together.
   *
   * @param values  Array of numbers. Maybe <code>null</code>.
   *
   * @return  Sum of these numbers.
   */
  public static int sum( @NonNull int ... values ) {
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
  public static double sum( @NonNull double ... values ) {
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
  public static long sum( @NonNull long ... values ) {
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
