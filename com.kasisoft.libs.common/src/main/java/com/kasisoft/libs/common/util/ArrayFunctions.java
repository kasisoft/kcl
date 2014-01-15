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
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Byte> asList( @NonNull byte ... values ) {
    List<Byte> result = new ArrayList<Byte>( values.length );
    for( byte v : values ) {
      result.add( Byte.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Short> asList( @NonNull short ... values ) {
    List<Short> result = new ArrayList<Short>( values.length );
    for( short v : values ) {
      result.add( Short.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Integer> asList( @NonNull int ... values ) {
    List<Integer> result = new ArrayList<Integer>( values.length );
    for( int v : values ) {
      result.add( Integer.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Long> asList( @NonNull long ... values ) {
    List<Long> result = new ArrayList<Long>( values.length );
    for( long v : values ) {
      result.add( Long.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Float> asList( @NonNull float ... values ) {
    List<Float> result = new ArrayList<Float>( values.length );
    for( float v : values ) {
      result.add( Float.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Double> asList( @NonNull double ... values ) {
    List<Double> result = new ArrayList<Double>( values.length );
    for( double v : values ) {
      result.add( Double.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Boolean> asList( @NonNull boolean ... values ) {
    List<Boolean> result = new ArrayList<Boolean>( values.length );
    for( boolean v : values ) {
      result.add( Boolean.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Character> asList( @NonNull char ... values ) {
    List<Character> result = new ArrayList<Character>( values.length );
    for( char v : values ) {
      result.add( Character.valueOf(v) );
    }
    return result;
  }
  
  /**
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Boolean[] toObjectArray( boolean[] values ) {
    if( (values == null) || (values.length == 0) ) {
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
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Byte[] toObjectArray( byte[] values ) {
    if( (values == null) || (values.length == 0) ) {
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
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Character[] toObjectArray( char[] values ) {
    if( (values == null) || (values.length == 0) ) {
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
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Short[] toObjectArray( short[] values ) {
    if( (values == null) || (values.length == 0) ) {
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
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Integer[] toObjectArray( int[] values ) {
    if( (values == null) || (values.length == 0) ) {
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
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Long[] toObjectArray( long[] values ) {
    if( (values == null) || (values.length == 0) ) {
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
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Float[] toObjectArray( float[] values ) {
    if( (values == null) || (values.length == 0) ) {
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
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   */
  public static Double[] toObjectArray( double[] values ) {
    if( (values == null) || (values.length == 0) ) {
      return null;
    } else {
      Double[] result = new Double[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = Double.valueOf( values[i] );
      }
      return result;
    }
  }
 
  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   */
  public static boolean[] toPrimitiveArray( Boolean[] values ) {
    values = cleanup( values );
    if( values == null ) {
      return null;
    } else {
      boolean[] result = new boolean[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = values[i].booleanValue();
      }
      return result;
    }
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   */
  public static byte[] toPrimitiveArray( Byte[] values ) {
    values = cleanup( values );
    if( values == null ) {
      return null;
    } else {
      byte[] result = new byte[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = values[i].byteValue();
      }
      return result;
    }
  }
  
  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   */
  public static char[] toPrimitiveArray( Character[] values ) {
    values = cleanup( values );
    if( values == null ) {
      return null;
    } else {
      char[] result = new char[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = values[i].charValue();
      }
      return result;
    }
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   */
  public static short[] toPrimitiveArray( Short[] values ) {
    values = cleanup( values );
    if( values == null ) {
      return null;
    } else {
      short[] result = new short[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = values[i].shortValue();
      }
      return result;
    }
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   */
  public static int[] toPrimitiveArray( Integer[] values ) {
    values = cleanup( values );
    if( values == null ) {
      return null;
    } else {
      int[] result = new int[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = values[i].intValue();
      }
      return result;
    }
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   */
  public static long[] toPrimitiveArray( Long[] values ) {
    values = cleanup( values );
    if( values == null ) {
      return null;
    } else {
      long[] result = new long[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = values[i].longValue();
      }
      return result;
    }
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   */
  public static float[] toPrimitiveArray( Float[] values ) {
    values = cleanup( values );
    if( values == null ) {
      return null;
    } else {
      float[] result = new float[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = values[i].floatValue();
      }
      return result;
    }
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   */
  public static double[] toPrimitiveArray( Double[] values ) {
    values = cleanup( values );
    if( values == null ) {
      return null;
    } else {
      double[] result = new double[ values.length ];
      for( int i = 0; i < values.length; i++ ) {
        result[i] = values[i].doubleValue();
      }
      return result;
    }
  }
  
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
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Boolean[] cleanup( Boolean ... values ) {
    return cleanup( values, Primitive.PBoolean );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Character[] cleanup( Character ... values ) {
    return cleanup( values, Primitive.PChar );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Byte[] cleanup( Byte ... values ) {
    return cleanup( values, Primitive.PByte );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Short[] cleanup( Short ... values ) {
    return cleanup( values, Primitive.PShort );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Integer[] cleanup( Integer ... values ) {
    return cleanup( values, Primitive.PInt );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Long[] cleanup( Long ... values ) {
    return cleanup( values, Primitive.PLong );
  }
  
  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Float[] cleanup( Float ... values ) {
    return cleanup( values, Primitive.PFloat );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   */
  public static Double[] cleanup( Double ... values ) {
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
   * 
   * @deprecated [15-Jan-2014:KASI]   This method will be removed with release 1.4. Use {@link #sumInt(int...)} instead. 
   */
  @Deprecated
  public static int sum( int ... values ) {
    return sumInt( values );
  }
  
  /**
   * Adds all array entries together.
   *
   * @param values  Array of numbers. Maybe <code>null</code>.
   *
   * @return  Sum of these numbers.
   */
  public static int sumInt( int ... values ) {
    int result = 0;
    if( (values != null) && (values.length > 0) ) {
      for( int i = 0; i < values.length; i++ ) {
        result += values[i];
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
   * 
   * @deprecated [15-Jan-2014:KASI]   This method will be removed with release 1.4. Use {@link #sumDouble(double...)} instead.  
   */
  @Deprecated
  public static double sum( double ... values ) {
    return sumDouble( values );
  }
  
  /**
   * Adds all array entries together.
   *
   * @param values  Array of numbers. Maybe <code>null</code>.
   *
   * @return  Sum of these numbers.
   */
  public static double sumDouble( double ... values ) {
    double result = 0;
    if( (values != null) && (values.length > 0) ) {
      for( int i = 0; i < values.length; i++ ) {
        result += values[i];
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
   * 
   * @deprecated [15-Jan-2014:KASI]   This method will be removed with release 1.4. Use {@link #sumLong(long...)} instead.  
   */
  @Deprecated
  public static long sum( long ... values ) {
    return sumLong( values );
  }
  
  /**
   * Adds all array entries together.
   *
   * @param values  Array of numbers. Maybe <code>null</code>.
   *
   * @return  Sum of these numbers.
   */
  public static long sumLong( long ... values ) {
    long result = 0;
    if( (values != null) && (values.length > 0) ) {
      for( int i = 0; i < values.length; i++ ) {
        result += values[i];
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
