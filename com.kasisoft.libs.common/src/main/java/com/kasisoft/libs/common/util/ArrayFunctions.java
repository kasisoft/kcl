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

import java.io.*;

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
   * Creates a new byte sequence while inserting one into a data block. If the index is outside of the destination no 
   * insertion takes place.
   * 
   * @param destination   The current data block which will be modified. Not <code>null</code>.
   * @param newsequence   The byte sequence which has to be inserted. Not <code>null</code>.
   * @param index         The location where to insert the byte sequence.
   * 
   * @return   The modified data block. Not <code>null</code>.
   */
  public static byte[] insert( @NonNull byte[] destination, @NonNull byte[] newsequence, int index ) {
    if( destination.length == 0 ) {
      return new byte[0];
    }
    if( index >= destination.length ) {
      return Arrays.copyOf( destination, destination.length );
    }
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    if( index > 0 ) {
      byteout.write( destination, 0, index );
    }
    if( newsequence.length > 0 ) {
      byteout.write( newsequence, 0, newsequence.length );
    }
    if( index < destination.length ) {
      byteout.write( destination, index, destination.length - index );
    }
    return byteout.toByteArray();
  }

  /**
   * Creates a new char sequence while inserting one into a data block. If the index is outside of the destination no 
   * insertion takes place.
   * 
   * @param destination   The current data block which will be modified. Not <code>null</code>.
   * @param newsequence   The char sequence which has to be inserted. Not <code>null</code>.
   * @param index         The location where to insert the char sequence.
   * 
   * @return   The modified data block. Not <code>null</code>.
   */
  public static char[] insert( @NonNull char[] destination, @NonNull char[] newsequence, int index ) {
    if( destination.length == 0 ) {
      return new char[0];
    }
    if( index >= destination.length ) {
      return Arrays.copyOf( destination, destination.length );
    }
    CharArrayWriter charout = new CharArrayWriter();
    if( index > 0 ) {
      charout.write( destination, 0, index );
    }
    if( newsequence.length > 0 ) {
      charout.write( newsequence, 0, newsequence.length );
    }
    if( index < destination.length ) {
      charout.write( destination, index, destination.length - index );
    }
    return charout.toCharArray();
  }
  
  /**
   * Joins the supplied buffers.
   * 
   * @param buffers   A list of buffers which has to be joined. Not <code>null</code>.
   * 
   * @return   A joined buffer. Not <code>null</code>.
   */
  public static byte[] joinBuffers( @NonNull byte[] ... buffers ) {
    int size = 0;
    for( int i = 0; i < buffers.length; i++ ) {
      if( buffers[i] != null ) {
        size += buffers[i].length;
      }
    }
    byte[] result = new byte[ size ];
    int    offset = 0;
    for( int i = 0; i < buffers.length; i++ ) {
      if( buffers[i] != null ) {
        System.arraycopy( buffers[i], 0, result, offset, buffers[i].length );
        offset += buffers[i].length;
      }
    }
    return result;
  }

  /**
   * Joins the supplied buffers.
   * 
   * @param buffers   A list of buffers which has to be joined. Not <code>null</code>.
   * 
   * @return   A joined buffer. Not <code>null</code>.
   */
  public static char[] joinBuffers( @NonNull char[] ... buffers ) {
    int size = 0;
    for( int i = 0; i < buffers.length; i++ ) {
      if( buffers[i] != null ) {
        size += buffers[i].length;
      }
    }
    char[] result = new char[ size ];
    int    offset = 0;
    for( int i = 0; i < buffers.length; i++ ) {
      if( buffers[i] != null ) {
        System.arraycopy( buffers[i], 0, result, offset, buffers[i].length );
        offset += buffers[i].length;
      }
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
   * Tries to find a byte sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the byte sequence or -1 in case there's no sequence.
   */
  public static int indexOf( @NonNull byte[] data, @NonNull byte[] sequence ) {
    return indexOf( data, sequence, 0 );
  }

  /**
   * Tries to find a byte sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * @param pos        The offset where to begin the search.
   * 
   * @return   The index of the byte sequence or -1 in case there's no sequence.
   */
  public static int indexOf( @NonNull byte[] buffer, @NonNull byte[] sequence, int pos ) {
    int last = buffer.length - sequence.length;
    if( (last < 0) || (pos > last) ) {
      // the sequence can't fit completely, so it's not available
      return -1;
    }
    for( int i = pos; i < last; i++ ) {
      if( buffer[i] == sequence[0] ) {
        // we're having a possible match, so compare the sequence
        if( compare( buffer, sequence, i ) ) {
          return i;
        }
      }
    }
    return -1;
  }
  
  /**
   * Tries to find the last byte sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the last byte sequence or -1 in case there's no sequence.
   */
  public static int lastIndexOf( @NonNull byte[] data, @NonNull byte[] sequence ) {
    return lastIndexOf( data, sequence, 0 );
  }

  /**
   * Tries to find the last byte sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * @param pos        The offset where to begin the search.
   * 
   * @return   The index of the last byte sequence or -1 in case there's no sequence.
   */
  public static int lastIndexOf( @NonNull byte[] buffer, @NonNull byte[] sequence, int pos ) {
    int last = buffer.length - sequence.length;
    if( (last < 0) || (pos > last) ) {
      // the sequence doesn't fit, so it's not available
      return -1;
    }
    for( int i = last; i >= pos; i-- ) {
      if( buffer[i] == sequence[0] ) {
        // we're having a possible match, so compare the sequence
        if( compare( buffer, sequence, i ) ) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Returns <code>true</code> if a byte sequence could be found at a specific position.
   * 
   * @param data        The data block where to search for. Not <code>null</code>.
   * @param tocompare   The byte sequence which have to be compared with. Not <code>null</code>.
   * @param offset      The offset within the data block where the sequence seems to be located.
   * 
   * @return   <code>true</code> <=> The byte sequence is available at the specified offset.
   */
  public static boolean compare( @NonNull byte[] data, @NonNull byte[] tocompare, int offset ) {
    for( int i = 0; i < tocompare.length; i++, offset++ ) {
      if( offset == data.length ) {
        // premature end of the comparison process
        return false;
      }
      if( data[ offset ] != tocompare[i] ) {
        return false;
      }
    }
    return true; 
  }
  
  /**
   * Tries to find a char sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The char sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the char sequence or -1 in case there's no sequence.
   */
  public static int indexOf( @NonNull char[] data, @NonNull char[] sequence ) {
    return indexOf( data, sequence, 0 );
  }
  
  /**
   * Tries to find a char sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The char sequence to search for. Not <code>null</code>.
   * @param pos        The offset where to begin the search.
   * 
   * @return   The index of the char sequence or -1 in case there's no sequence.
   */
  public static int indexOf( @NonNull char[] buffer, @NonNull char[] sequence, int pos ) {
    int last = buffer.length - sequence.length;
    if( (last < 0) || (pos > last) ) {
      // the sequence can't fit completely, so it's not available
      return -1;
    }
    for( int i = pos; i < last; i++ ) {
      if( buffer[i] == sequence[0] ) {
        // we're having a possible match, so compare the sequence
        if( compare( buffer, sequence, i ) ) {
          return i;
        }
      }
    }
    return -1;
  }
  
  /**
   * Tries to find the last char sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The char sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the last char sequence or -1 in case there's no sequence.
   */
  public static int lastIndexOf( @NonNull char[] data, @NonNull char[] sequence ) {
    return lastIndexOf( data, sequence, 0 );
  }

  /**
   * Tries to find the last character sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The character sequence to search for. Not <code>null</code>.
   * @param pos        The offset where to begin the search.
   * 
   * @return   The index of the last character sequence or -1 in case there's no sequence.
   */
  public static int lastIndexOf( @NonNull char[] buffer, @NonNull char[] sequence, int pos ) {
    int last = buffer.length - sequence.length;
    if( (last < 0) || (pos > last) ) {
      // the sequence doesn't fit, so it's not available
      return -1;
    }
    for( int i = last; i >= pos; i-- ) {
      if( buffer[i] == sequence[0] ) {
        // we're having a possible match, so compare the sequence
        if( compare( buffer, sequence, i ) ) {
          return i;
        }
      }
    }
    return -1;
  }
  
  /**
   * Returns true if a char sequence could be found at a specific position.
   * 
   * @param data        The data block where to search for. Not <code>null</code>.
   * @param tocompare   The char sequence which have to be compared with. Not <code>null</code>.
   * @param offset      The offset within the data block where the sequence seems to be located.
   * 
   * @return   <code>true</code> <=> The char sequence is available at the specified offset.
   */
  public static boolean compare( @NonNull char[] data, @NonNull char[] tocompare, int offset ) {
    for( int i = 0; i < tocompare.length; i++, offset++ ) {
      if( offset == data.length ) {
        // premature end of the comparison process
        return false;
      }
      if( data[ offset ] != tocompare[i] ) {
        return false;
      }
    }
    return true; 
  }
  
  /**
   * Copies a small range from a specific array.
   * 
   * @param source   The array providing the input data. Not <code>null</code>.
   * @param offset   The offset of the starting point within the input data. Must be in the range [0..source.length(
   * @param length   The amount of bytes which have to be copied. Must be greater than 0.
   * 
   * @return   A copy of the desired range. Neither <code>null</code> nor empty.
   */
  public static byte[] copyRange( @NonNull byte[] source, int offset, int length ) {
    byte[] result = new byte[ length ];
    System.arraycopy( source, offset, result, 0, length );
    return result;
  }

  /**
   * Copies a small range from a specific array.
   * 
   * @param source   The array providing the input data. Not <code>null</code>.
   * @param offset   The offset of the starting point within the input data. Must be in the range [0..source.length(
   * @param length   The amount of bytes which have to be copied. Must be greater than 0.
   * 
   * @return   A copy of the desired range. Neither <code>null</code> nor empty.
   */
  public static char[] copyRange( @NonNull char[] source, int offset, int length ) {
    char[] result = new char[ length ];
    System.arraycopy( source, offset, result, 0, length );
    return result;
  }

  /**
   * Joins the supplied arrays to a single one. It is legal to supply <code>null</code> values.
   * 
   * @param arrays   The arrays that are supplied to be joined. Maybe <code>null</code>.
   * 
   * @return   A joined array. Not <code>null</code>.
   */
  @SuppressWarnings("null")
  public static <T> T[] join( T[] ... arrays ) {
    int length = 0;
    int j      = -1;
    if( arrays != null ) {
      for( int i = arrays.length - 1; i >= 0; i-- ) {
        if( arrays[i] != null ) {
          length += arrays[i].length;
          j       = i;
        }
      }
    }
    if( length == 0 ) {
      return (T[]) Arrays.<T>asList().toArray();
    }
    T[] result = Arrays.copyOf( arrays[j], length );
    int offset = arrays[j].length;
    for( int i = j + 1; i < arrays.length; i++ ) {
      if( arrays[i] != null ) {
        System.arraycopy( arrays[i], 0, result, offset, arrays[i].length );
        offset += arrays[i].length;
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
