package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;

import lombok.*;

import java.util.*;

import java.io.*;

/**
 * Collection of functions useful in conjunction with arrays.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
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
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static List<Byte> asList( @NonNull byte ... values ) {
    return Primitive.PByte.toList( values );
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static List<Short> asList( @NonNull short ... values ) {
    return Primitive.PShort.toList( values );
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static List<Integer> asList( @NonNull int ... values ) {
    return Primitive.PInt.toList( values );
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static List<Long> asList( @NonNull long ... values ) {
    return Primitive.PLong.toList( values );
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static List<Float> asList( @NonNull float ... values ) {
    return Primitive.PFloat.toList( values );
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static List<Double> asList( @NonNull double ... values ) {
    return Primitive.PDouble.toList( values );
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static List<Boolean> asList( @NonNull boolean ... values ) {
    return Primitive.PBoolean.toList( values );
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static List<Character> asList( @NonNull char ... values ) {
    return Primitive.PChar.toList( values );
  }
  
  /**
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static Boolean[] toObjectArray( boolean[] values ) {
    return Primitive.PBoolean.toObjectArray( values );
  }

  /**
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static Byte[] toObjectArray( byte[] values ) {
    return Primitive.PByte.toObjectArray( values );
  }
  
  /**
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static Character[] toObjectArray( char[] values ) {
    return Primitive.PChar.toObjectArray( values );
  }

  /**
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static Short[] toObjectArray( short[] values ) {
    return Primitive.PShort.toObjectArray( values );
  }

  /**
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static Integer[] toObjectArray( int[] values ) {
    return Primitive.PInt.toObjectArray( values );
  }

  /**
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static Long[] toObjectArray( long[] values ) {
    return Primitive.PLong.toObjectArray( values );
  }

  /**
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static Float[] toObjectArray( float[] values ) {
    return Primitive.PFloat.toObjectArray( values );
  }

  /**
   * This method casts the supplied primitives into the corresponding object types. Empty arrays will result in 
   * <code>null</code> return values.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding object types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static Double[] toObjectArray( double[] values ) {
    return Primitive.PDouble.toObjectArray( values );
  }
 
  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static boolean[] toPrimitiveArray( Boolean[] values ) {
    return Primitive.PBoolean.toPrimitiveArray( values );
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static byte[] toPrimitiveArray( Byte[] values ) {
    return Primitive.PByte.toPrimitiveArray( values );
  }
  
  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static char[] toPrimitiveArray( Character[] values ) {
    return Primitive.PChar.toPrimitiveArray( values );
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static short[] toPrimitiveArray( Short[] values ) {
    return Primitive.PShort.toPrimitiveArray( values );
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static int[] toPrimitiveArray( Integer[] values ) {
    return Primitive.PInt.toPrimitiveArray( values );
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static long[] toPrimitiveArray( Long[] values ) {
    return Primitive.PLong.toPrimitiveArray( values );
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static float[] toPrimitiveArray( Float[] values ) {
    return Primitive.PFloat.toPrimitiveArray( values );
  }

  /**
   * This method casts the supplied objects into their corresponding primitive types. <code>null</code> values will be
   * ignored. If there's no value at all <code>null</code> will be returned.
   * 
   * @param values   The primitives which shall be casted. Maybe <code>null</code>.
   * 
   * @return   The corresponding primitive types. Maybe <code>null</code>.
   * 
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#toPrimitiveArray
   */
  @Deprecated
  public static double[] toPrimitiveArray( Double[] values ) {
    return Primitive.PDouble.toPrimitiveArray( values );
  }
  
  /**
   * Counts the number of non-<code>null</code> values within the supplied array.
   * 
   * @param objects   The array which values have to be investigated. Maybe <code>null</code>.
   * 
   * @return   The number of non-<code>null</code> values.
   *           
   * @deprecated [17-JUL-2018:KASI]   Use Primitive#countSet
   */
  @Deprecated
  public static <T> int nonNullLength( T ... objects ) {
    Primitive primitive = Primitive.byType( objects );
    if( primitive == null ) {
      return 0;
    } else {
      return primitive.countSet( objects );
    }
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   *           
   * @deprecated [17-JUL-2018:KASI]   Use Primitive.PBoolean#cleanup instead.
   */
  @Deprecated
  public static Boolean[] cleanup( Boolean ... values ) {
    return Primitive.PBoolean.cleanup( values );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   *           
   * @deprecated [17-JUL-2018:KASI]   Use Primitive.PChar#cleanup instead.
   */
  @Deprecated
  public static Character[] cleanup( Character ... values ) {
    return Primitive.PChar.cleanup( values );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   *           
   * @deprecated [17-JUL-2018:KASI]   Use Primitive.PByte#cleanup instead.
   */
  @Deprecated
  public static Byte[] cleanup( Byte ... values ) {
    return Primitive.PByte.cleanup( values );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   *           
   * @deprecated [17-JUL-2018:KASI]   Use Primitive.PShort#cleanup instead.
   */
  @Deprecated
  public static Short[] cleanup( Short ... values ) {
    return Primitive.PShort.cleanup( values );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   *           
   * @deprecated [17-JUL-2018:KASI]   Use Primitive.PLong#cleanup instead.
   */
  @Deprecated
  public static Integer[] cleanup( Integer ... values ) {
    return Primitive.PInt.cleanup( values );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   *           
   * @deprecated [17-JUL-2018:KASI]   Use Primitive.PLong#cleanup instead.
   */
  @Deprecated
  public static Long[] cleanup( Long ... values ) {
    return Primitive.PLong.cleanup( values );
  }
  
  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   *           
   * @deprecated [17-JUL-2018:KASI]   Use Primitive.PFloat#cleanup instead.
   */
  @Deprecated
  public static Float[] cleanup( Float ... values ) {
    return Primitive.PFloat.cleanup( values );
  }

  /**
   * Performs a cleanup for the supplied values which means to get rid of each <code>null</code> elements.
   * 
   * @param values   The values that might to be cleaned up. Maybe <code>null</code>.
   * 
   * @return   A cleaned array (the argument if there was no <code>null</code> value) or <code>null</code> if there was
   *           no non-<code>null</code> values.
   *           
   * @deprecated [17-JUL-2018:KASI]   Use Primitive.PDouble#cleanup instead.
   */
  @Deprecated
  public static Double[] cleanup( Double ... values ) {
    return Primitive.PDouble.cleanup( values );
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
    return new ArrayTraversal<>( input );
  }

  /**
   * Returns an Iterator which is usable to iterate through the supplied array.
   * 
   * @param input   The array which should be traversed. Not <code>null</code>.
   * 
   * @return   The Iterator which is used to traverse the array.
   */
  public static <T> Iterator<T> iterator( @NonNull T ... input ) {
    return new ArrayTraversal<>( input );
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
   * @param defaultval   The default value that will be used if the joined outcome won't contain any elements. 
   *                     Maybe <code>null</code>.
   * @param arrays       The arrays that are supplied to be joined. Maybe <code>null</code>.
   * 
   * @return   A joined array. Maybe <code>null</code>.
   */
  @SuppressWarnings("null")
  public static <T> T[] joinArrays( T[] defaultval, T[] ... arrays ) {
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
      return defaultval;
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
