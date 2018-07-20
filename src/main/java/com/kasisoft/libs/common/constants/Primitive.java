package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.function.*;

import java.util.function.*;

import java.util.*;

import java.lang.ref.*;
import java.lang.reflect.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Declarations used to identify primitive types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Primitive<PA, O> implements Comparable<Primitive>{

  public static Primitive <boolean [], Boolean  > PBoolean = new Primitive<>( "PBoolean"  , boolean . class , boolean [] . class , Boolean   . class , Boolean   [] . class , Primitive::toPrimitive, Primitive::toObject, Primitive::isEqual, Primitive::randomBoolean , Arrays::copyOfRange, null               , 0                   , 0                   );
  public static Primitive <byte    [], Byte     > PByte    = new Primitive<>( "PByte"     , byte    . class , byte    [] . class , Byte      . class , Byte      [] . class , Primitive::toPrimitive, Primitive::toObject, Primitive::isEqual, Primitive::randomByte    , Arrays::copyOfRange, Primitive::isBigger, Byte    . MIN_VALUE , Byte    . MAX_VALUE );
  public static Primitive <char    [], Character> PChar    = new Primitive<>( "PChar"     , char    . class , char    [] . class , Character . class , Character [] . class , Primitive::toPrimitive, Primitive::toObject, Primitive::isEqual, Primitive::randomChar    , Arrays::copyOfRange, null               , 0                   , 0                   );
  public static Primitive <short   [], Short    > PShort   = new Primitive<>( "PShort"    , short   . class , short   [] . class , Short     . class , Short     [] . class , Primitive::toPrimitive, Primitive::toObject, Primitive::isEqual, Primitive::randomShort   , Arrays::copyOfRange, Primitive::isBigger, Short   . MIN_VALUE , Short   . MAX_VALUE );
  public static Primitive <int     [], Integer  > PInt     = new Primitive<>( "PInt"      , int     . class , int     [] . class , Integer   . class , Integer   [] . class , Primitive::toPrimitive, Primitive::toObject, Primitive::isEqual, Primitive::randomInt     , Arrays::copyOfRange, Primitive::isBigger, Integer . MIN_VALUE , Integer . MAX_VALUE );
  public static Primitive <long    [], Long     > PLong    = new Primitive<>( "PLong"     , long    . class , long    [] . class , Long      . class , Long      [] . class , Primitive::toPrimitive, Primitive::toObject, Primitive::isEqual, Primitive::randomLong    , Arrays::copyOfRange, Primitive::isBigger, Long    . MIN_VALUE , Long    . MAX_VALUE );
  public static Primitive <float   [], Float    > PFloat   = new Primitive<>( "PFloat"    , float   . class , float   [] . class , Float     . class , Float     [] . class , Primitive::toPrimitive, Primitive::toObject, Primitive::isEqual, Primitive::randomFloat   , Arrays::copyOfRange, Primitive::isBigger, 0                   , 0                   );
  public static Primitive <double  [], Double   > PDouble  = new Primitive<>( "PDouble"   , double  . class , double  [] . class , Double    . class , Double    [] . class , Primitive::toPrimitive, Primitive::toObject, Primitive::isEqual, Primitive::randomDouble  , Arrays::copyOfRange, Primitive::isBigger, 0                   , 0                   );
  
  @Getter 
  Class<?>                                  primitiveClass;
  
  @Getter 
  Class<PA>                                 arrayClass;
  
  @Getter 
  Class<O>                                  objectClass;
  
  @Getter 
  Class<O[]>                                objectArrayClass;
  
  @Getter 
  long                                      min;
  
  @Getter 
  long                                      max;
  
  InternalBuffers<PA>                       ibuffers;
  boolean                                   supportsMinMax;
  String                                    name;
  BiConsumer<O[], PA>                       toPrimitive;
  BiConsumer<PA, O[]>                       toObject;
  QPredicate<PA>                            isEqual;
  Supplier                                  randomValue;
  TriFunction<PA, Integer, Integer, PA>     copy;
  PA                                        empty;
  BiPredicate<O, O>                         isBigger;
  BiPredicate<O, O>                         isSmaller;
  
  private Primitive( 
    String                                      pname, 
    Class<?>                                    primitive, 
    Class<PA>                                   arraytype, 
    Class<O>                                    objclazz, 
    Class<O[]>                                  objclazzarray, 
    BiConsumer<O[], PA>                         toprimitive, 
    BiConsumer<PA, O[]>                         toobject, 
    QPredicate<PA>                              isequal, 
    Supplier                                    randomvalue, 
    TriFunction<PA, Integer, Integer, PA>       copyfunction,
    BiPredicate<O, O>                           isbigger,
    long                                        minval, 
    long                                        maxval 
  ) {
    name              = pname;
    primitiveClass    = primitive;
    arrayClass        = arraytype;
    objectClass       = objclazz;
    objectArrayClass  = objclazzarray;
    toPrimitive       = toprimitive;
    toObject          = toobject;
    isEqual           = isequal;
    randomValue       = randomvalue;
    copy              = copyfunction;
    min               = minval;
    max               = maxval;
    supportsMinMax    = minval != maxval;
    empty             = (PA) Array.newInstance( primitiveClass, 0 );
    isBigger          = isbigger;
    isSmaller         = isBigger != null ? isBigger.negate() : null;
    ibuffers          = new InternalBuffers<PA>( this::newArray, this::length );
    Number.class.isAssignableFrom( objectClass );
    LocalData.primitivemap.put( primitive        , this );
    LocalData.primitivemap.put( objclazz         , this );
    LocalData.primitivemap.put( arraytype        , this );
    LocalData.primitivemap.put( objectArrayClass , this );
  }
  
  /**
   * Tries to find a char sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The char sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the char sequence or -1 in case there's no sequence.
   */
  public int indexOf( @NonNull PA buffer, @NonNull PA sequence ) {
    return indexOf( buffer, sequence, 0 );
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
  public int indexOf( @NonNull PA buffer, @NonNull PA sequence, int pos ) {
    return indexOfOp( buffer, sequence, pos, true, ($idx, $val) -> $idx < $val, $ -> $ + 1 );
  }
  
  /**
   * Tries to find the last byte sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the last byte sequence or -1 in case there's no sequence.
   */
  public int lastIndexOf( @NonNull PA buffer, @NonNull PA sequence ) {
    return lastIndexOf( buffer, sequence, 0 );
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
  public int lastIndexOf( @NonNull PA buffer, @NonNull PA sequence, int pos ) {
    return indexOfOp( buffer, sequence, pos, false, ($idx, $val) -> $idx >= $val, $ -> $ - 1 );
  }
  
  private int indexOfOp( PA buffer, PA sequence, int pos, boolean first, BiPredicate<Integer, Integer> test, Function<Integer, Integer> op ) {
    int bufferlength = length( buffer  );
    int seqlength    = length( sequence );
    int last         = bufferlength - seqlength;
    if( (last < 0) || (pos > last) ) {
      // the sequence doesn't fit, so it's not available
      return -1;
    }
    int start    = first ? pos  : last;
    int boundary = first ? last : pos;
    for( int i = start; test.test(i, boundary); i = op.apply(i) ) {
      if( isEqual.test( buffer, i, sequence, 0 ) ) {
        // we're having a possible match, so compare the sequence
        if( compare( buffer, sequence, i ) ) {
          return i;
        }
      }
    }
    return -1;
  }
  
  /**
   * Creates a new byte sequence while inserting one into a data block. If the index is outside of the destination no 
   * insertion takes place.
   * 
   * @param source      The current data block which will be modified. Not <code>null</code>.
   * @param additional  The byte sequence which has to be inserted. Not <code>null</code>.
   * @param index       The location where to insert the byte sequence.
   * 
   * @return   The modified data block. Not <code>null</code>.
   */
  public PA insert( @NonNull PA source, @NonNull PA additional, int index ) {
    int destinationlength = length( source );
    int additionallength  = length( additional );
    if( (destinationlength == 0) || (additionallength == 0) ) {
      return empty;
    }
    if( (index < 0) || (index >= destinationlength) ) {
      return empty;
    }
    int totalsize = destinationlength + additionallength;
    PA  result    = newArray( totalsize );
    int offset    = 0;
    if( index > 0 ) {
      System.arraycopy( source, 0, result, offset, index );
      offset += index;
    }
    System.arraycopy( additional, 0, result, offset, additionallength );
    offset += additionallength;
    if( index < destinationlength ) {
      System.arraycopy( source, index, result, offset, destinationlength - index );
    }
    return result;
  }

  public <R> R forValues( O[] values, BiFunction<O, R, R> func ) {
    return forValues( values, null, func );
  }
  
  public <R> R forValues( O[] values, R initial, BiFunction<O, R, R> func ) {
    R   result = initial;
    int length = length( values );
    for( int i = 0; i < length; i++ ) {
      result = func.apply( values[i], result );
    }
    return result;
  }

  public <R> R forValues( PA values, BiFunction<O, R, R> func ) {
    return forValues( values, null, func );
  }
  
  public <R> R forValues( PA values, R initial, BiFunction<O, R, R> func ) {
    R   result = initial;
    int length = length( values );
    for( int i = 0; i < length; i++ ) {
      result = func.apply( (O) Array.get( values, i ), result );
    }
    return result;
  }
  
  public O or( PA values ) {
    return booleanOp( values, $ -> $, Boolean.FALSE, Boolean.TRUE);
  }

  public O and( PA values ) {
    return booleanOp( values, $ -> !$, Boolean.TRUE, Boolean.FALSE );
  }

  public O or( O[] values ) {
    return booleanOp( values, $ -> $, Boolean.FALSE, Boolean.TRUE);
  }

  public O and( O[] values ) {
    return booleanOp( values, $ -> !$, Boolean.TRUE, Boolean.FALSE );
  }

  private O booleanOp( PA values, Predicate<Boolean> test, Boolean success, Boolean failure ) {
    if( primitiveClass != boolean.class ) {
      return null;
    }
    boolean[] array = (boolean[]) values;
    if( array.length == 0 ) {
      return null;
    }
    for( int i = 0; i < array.length; i++ ) {
      if( test.test( array[i] ) ) {
        return (O) failure;
      }
    }
    return (O) success;
  }
  
  private O booleanOp( O[] values, Predicate<Boolean> test, Boolean success, Boolean failure ) {
    if( primitiveClass != boolean.class ) {
      return null;
    }
    Boolean[] array = (Boolean[]) values;
    if( array.length == 0 ) {
      return null;
    }
    for( int i = 0; i < array.length; i++ ) {
      if( (array[i] != null) && test.test( array[i] ) ) {
        return (O) failure;
      }
    }
    return (O) success;
  }

  public O max( PA values ) {
    O   result = null;
    int length = length( values );
    if( (isBigger != null) && (length > 0) ) {
      result = (O) Array.get( values, 0 );
      if( length > 1 ) {
        for( int i = 1; i < length; i++ ) {
          O current = (O) Array.get( values, i );
          if( isBigger.test( current, result ) ) {
            result = current;
          }
        }
      }
    }
    return result;
  }

  public O min( PA values ) {
    O   result = null;
    int length = length( values );
    if( (isBigger != null) && (length > 0) ) {
      result = (O) Array.get( values, 0 );
      if( length > 1 ) {
        for( int i = 1; i < length; i++ ) {
          O current = (O) Array.get( values, i );
          if( isSmaller.test( current, result ) ) {
            result = current;
          }
        }
      }
    }
    return result;
  }

  public PA concat( PA ... parts ) {
    PA  result = empty;
    if( (parts != null) && (parts.length > 0) ) {
      int total  = 0;
      for( PA part : parts ) {
        total += length( part );
      }
      result  = (PA) Array.newInstance( primitiveClass, total );
      int idx = 0;
      for( PA part : parts ) {
        int size = length( part );
        if( size > 0 ) {
          System.arraycopy( part, 0, result, idx, size );
          idx += size;
        }
      }
    }
    return result;
  }
       
  public PA copy( @NonNull PA input ) {
    int length = length( input );
    return copy( input, length );
  }

  public PA copy( @NonNull PA input, int newlength ) {
    return copy.apply( input, 0, newlength );
  }

  public PA copyOfRange( @NonNull PA input, int from, int to ) {
    return copy.apply( input, from, to );
  }

  public O randomValue() {
    return (O) randomValue.get();
  }

  public PA randomArray( int length ) {
    return randomArray( length, null );
  }
  
  public PA randomArray( int length, Supplier<O> random ) {
    if( random == null ) {
      random = this::randomValue;
    }
    PA result = (PA) Array.newInstance( primitiveClass, length );
    for( int i = 0; i < length; i++ ) {
      Array.set(result, i, random.get() );
    }
    return result;
  }

  public boolean compare( @NonNull PA data, @NonNull PA tocompare ) {
    int l1 = length( data );
    if( l1 == 0 ) {
      return false;
    }
    return compare( data, tocompare, 0 );
  }
  
  public boolean compare( @NonNull PA data, @NonNull PA tocompare, int offset ) {
    int datalength  = length( data      );
    int length      = length( tocompare );
    for( int i = 0; i < length; i++, offset++ ) {
      if( offset == datalength ) {
        // premature end of the comparison process
        return false;
      }
      if( ! isEqual.test( data, offset, tocompare, i ) ) {
        return false;
      }
    }
    return true; 
  }
  
  public PA cleanup( PA input ) {
    PA result = null;
    return result;
  }
  
  public O[] cleanup( O[] input ) {
    return cleanup( input, null );
  }
  
  public O[] cleanup( O[] input, Predicate<O> isNotSet ) {
    O[] result = null;
    if( input != null ) {
      if( isNotSet == null ) {
        isNotSet = $ -> $ == null;
      }
      Predicate<O> isSet = isNotSet.negate();
      int count = countUnset( input, isNotSet );
      if( count == 0 ) {
        result = input;
      } else if( count < input.length ) {
        result = newObjectArray( input.length - count );
        for( int i = 0, j = 0; i < input.length; i++ ) {
          if( isSet.test( input[i] ) ) {
            result[j++] = input[i];
          }
        }
      }
    }
    return result;
  }

  public int countSet( O[] input ) {
    return countSet( input, null );
  }
  
  public int countSet( O[] input, Predicate<O> isSet ) {
    if( isSet == null ) {
      isSet = $ -> $ != null;
    }
    return count( input, isSet );
  }
  
  public int countUnset( O[] input ) {
    return countUnset( input, null );
  }
  
  public int countUnset( O[] input, Predicate<O> isNotSet ) {
    if( isNotSet == null ) {
      isNotSet = $ -> $ == null;
    }
    return count( input, isNotSet );
  }

  private int count( O[] input, Predicate<O> test ) {
    int result = 0;
    for( int i = 0; i < input.length; i++ ) {
      if( test.test( input[i] ) ) {
        result++;
      }
    }
    return result;
  }

  public List<O> toList( PA primitiveArray ) {
    return new ArrayList<>( Arrays.asList( toObjectArray( primitiveArray ) ) );
  }

  public O[] toObjectArray( PA primitiveArray ) {
    O[] result = null;
    if( primitiveArray != null ) {
      int length  = length( primitiveArray );
      result      = newObjectArray( length );
      toObject.accept( primitiveArray, result );
    }
    return result;
  }

  public PA toPrimitiveArray( O[] objectArray ) {
    PA result = null;
    if( objectArray != null ) {
      objectArray = cleanup( objectArray );
      int length  = length( objectArray );
      result      = newArray( length );
      toPrimitive.accept( objectArray, result );
    }
    return result;
  }

  /**
   * Returns <code>true</code> if this type supports the usage of {@link #getMin()} and {@link #getMax()}.
   * 
   * @return   <code>true</code> <=> Using {@link #getMin()} and {@link #getMax()} is supported.
   */
  public boolean supportsMinMax() {
    return supportsMinMax;
  }
  
  /**
   * Returns the unsigned maximum value for this type. Not supported for PChar, PBoolean, PFloat, PDouble and PLong.
   * 
   * @return   The unsigned maximum value for this type. 
   */
  public long getUnsignedMax() {
    if( this == PLong ) {
      return 0;
    }
    return 2 * max + 1;
  }
  
  /**
   * Returns an array of this type consisting of the supplied number of items.
   * 
   * @param size   The number of items.
   * 
   * @return   The array of this type. Not <code>null</code>.
   */
  public PA newArray( int size ) {
    return (PA) Array.newInstance( primitiveClass, size );
  }

  /**
   * Returns an array of this type consisting of the supplied number of items.
   * 
   * @param size   The number of items.
   * 
   * @return   The array of the corresponding object type. Not <code>null</code>.
   */
  public O[] newObjectArray( int size ) {
    return (O[]) Array.newInstance( objectClass, size );
  }

  /**
   * Returns the length of an array instance.
   * 
   * @param arrayobj    An array instance. Maybe <code>null</code>.
   * 
   * @return   The length of an array instance.
   */
  public int length( Object arrayobj ) {
    if( arrayobj != null ) {
      return Array.getLength( arrayobj );
    } else {
      return 0;
    }
  }
  
  /**
   * Delivers the primitive type associated with the supplied object. The supplied object may be an object type, an 
   * array of the primitive type or an array of the object type.
   * 
   * @param obj   The value which primitive equivalent should be returned. Maybe <code>null</code>.
   * 
   * @return   The primitive equivalent for the supplied type. Maybe <code>null</code>.
   */
  public static Primitive byType( Object obj ) {
    if( obj == null ) {
      return null;
    } else {
      return LocalData.primitivemap.get( obj.getClass() );
    }
  }
  
  /**
   * Allocates a block with the current default size.
   * 
   * @return  A block with the current default size. Neither <code>null</code> nor empty.
   */
  public synchronized PA allocate() {
    return allocate( null );
  }
  
  /**
   * Allocates a block with a specified size. The returned block doesn't necessarily have the desired size so the 
   * returned block might be larger.
   * 
   * @param size   The requested size of the returned buffer. A value of <code>null</code> means that the default size 
   *               has to be used (see {@link CommonProperty#BufferCount}).
   * 
   * @return   A block with the requested size. Neither <code>null</code> nor empty.
   */
  @SuppressWarnings("unchecked")
  public synchronized PA allocate( Integer size ) {
    return ibuffers.allocate( size );
  }  
  
  /**
   * Releases the supplied byte array so it can be reallocated later.
   * 
   * @param data   The data that can be reallocated later. Not <code>null</code>.
   */
  public synchronized void release( @NonNull PA data ) {
    ibuffers.release( data );
  }

  /**
   * Executes the supplied function while providing a buffer.
   * 
   * @param function   The function that will be executed using the buffer. Not <code>null</code>.
   * 
   * @return   The result of the supplied function. Maybe <code>null</code>.
   */
  public <R> R withBuffer( @NonNull Function<PA, R> function ) {
    return withBuffer( null, function );
  }

  /**
   * Executes the supplied function while providing a buffer.
   *
   * @param size       The requested size of the returned buffer. A value of <code>null</code> means that the default 
   *                   size has to be used (see {@link CommonProperty#BufferCount}).
   * @param function   The function that will be executed using the buffer. Not <code>null</code>.
   * 
   * @return   The result of the supplied function. Maybe <code>null</code>.
   */
  public <R> R withBuffer( Integer size, Function<PA, R> function ) {
    PA buffer = allocate( size );
    try {
      return function.apply( buffer );
    } finally {
      release( buffer );
    }
  }

  /**
   * Executes the supplied function while providing a buffer.
   * 
   * @param function   The function that will be executed using the buffer. Not <code>null</code>.
   */
  public void withBufferDo( Consumer<PA> consumer ) {
    withBufferDo( null, consumer ); 
  }

  /**
   * Executes the supplied function while providing a buffer.
   * 
   * @param size       The requested size of the returned buffer. A value of <code>null</code> means that the default 
   *                   size has to be used (see {@link CommonProperty#BufferCount}).
   * @param function   The function that will be executed using the buffer. Not <code>null</code>.
   */
  public void withBufferDo( Integer size, Consumer<PA> consumer ) {
    PA buffer = allocate( size );
    try {
      consumer.accept( buffer );
    } finally {
      release( buffer );
    }
  }
  
  @Override
  public int compareTo(Primitive o) {
    return name.compareTo( o.name );
  }

  @Override
  public String toString() {
    return name;
  }
  
  private static boolean isEqual( boolean[] dest1, int idx1, boolean[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  private static boolean isEqual( byte[] dest1, int idx1, byte[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  private static boolean isEqual( char[] dest1, int idx1, char[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  private static boolean isEqual( short[] dest1, int idx1, short[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  private static boolean isEqual( int[] dest1, int idx1, int[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  private static boolean isEqual( long[] dest1, int idx1, long[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  private static boolean isEqual( float[] dest1, int idx1, float[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  private static boolean isEqual( double[] dest1, int idx1, double[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }
  
  private static void toPrimitive( Boolean[] array, boolean[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toPrimitive( Byte[] array, byte[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toPrimitive( Character[] array, char[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toPrimitive( Short[] array, short[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toPrimitive( Integer[] array, int[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toPrimitive( Long[] array, long[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toPrimitive( Float[] array, float[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toPrimitive( Double[] array, double[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toObject( boolean[] array, Boolean[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toObject( byte[] array, Byte[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toObject( char[] array, Character[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toObject( short[] array, Short[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toObject( int[] array, Integer[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toObject( long[] array, Long[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toObject( float[] array, Float[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  private static void toObject( double[] array, Double[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }
  
  private static double randomVal() {
    return Math.random() * System.currentTimeMillis();
  }

  private static boolean randomBoolean() {
    long val = (long) randomVal();
    return (boolean) ((val % 2) == 0);
  }

  private static double randomDouble() {
    return (randomBoolean() ? 1.0 : -1.0) * randomVal();
  }
  
  private static float randomFloat() {
    return (float) randomDouble();
  }

  private static long randomLong() {
    return (long) randomDouble();
  }

  private static int randomInt() {
    return (int) randomLong();
  }

  private static short randomShort() {
    return (short) randomLong();
  }

  private static byte randomByte() {
    return (byte) randomLong();
  }

  private static char randomChar() {
    return (char) randomShort();
  }
  
  private static boolean isBigger( Byte c1, Byte c2 ) {
    return c1.byteValue() > c2.byteValue();
  }

  private static boolean isBigger( Short c1, Short c2 ) {
    return c1.shortValue() > c2.shortValue();
  }

  private static boolean isBigger( Integer c1, Integer c2 ) {
    return c1.intValue() > c2.intValue();
  }
  
  private static boolean isBigger( Long c1, Long c2 ) {
    return c1.longValue() > c2.longValue();
  }
  
  private static boolean isBigger( Float c1, Float c2 ) {
    return c1.floatValue() > c2.floatValue();
  }

  private static boolean isBigger( Double c1, Double c2 ) {
    return c1.doubleValue() > c2.doubleValue();
  }

  public static Primitive[] values() {
    Primitive[] result = LocalData.primitivemap.values().toArray( new Primitive[ LocalData.primitivemap.size() ] );
    Arrays.sort( result );
    return result;
  }
  
  private static class LocalData {
    
    private static Map<Class<?>, Primitive>   primitivemap = new Hashtable<>();
    
  } /* ENDCLASS */
  
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class InternalBuffers<PA> implements Comparator<SoftReference<PA>> {

    List<SoftReference<PA>>        allocated;
    
    Function<Integer, PA>          newArray;
    Function<PA, Integer>          length;
    
    private InternalBuffers( Function<Integer, PA> createArray, Function<PA, Integer> getlength ) {
      newArray    = createArray;
      length      = getlength;
      allocated   = new ArrayList<>();
    }

    @Override
    public int compare( SoftReference<PA> o1, SoftReference<PA> o2 ) {
      int l1 = length.apply( o1.get() );
      int l2 = length.apply( o2.get() );
      if( (l1 == 0) || (l2 == 0) ) {
        // this element have been cleared by the GC so return 0 in order to sustain the current position
        return 0;
      }
      return l1 - l2;
    }

    private synchronized void cleanup() {
      // remove all empty soft references
      allocated.removeIf( $ -> $.get() == null );
    }
    
    /**
     * Returns a block of data matching the supplied size constraint.
     * 
     * @param size   The size which is requested.
     * 
     * @return   The block with the apropriate space or <code>null</code>.
     */
    private synchronized PA getBlock( int size ) {
      if( allocated.isEmpty() ) {
        return null;
      }
      for( int i = 0; i < allocated.size(); i++ ) {
        SoftReference<PA> ref    = allocated.get(i);
        PA                result = ref.get();
        if( length.apply( result ) >= size ) {
          // we're giving away this memory block, so drop the reference
          ref.clear();
          allocated.remove( ref );
          return result;
        }
      }
      return null;
    }
    
    /**
     * Allocates a block with a specified size. The returned block doesn't necessarily have the desired size so the 
     * returned block might be larger.
     * 
     * @param size   The requested size of the returned buffer. A value of <code>null</code> means that the default size 
     *               has to be used (see {@link CommonProperty#BufferCount}).
     * 
     * @return   A block with the requested size. Neither <code>null</code> nor empty.
     */
    @SuppressWarnings("unchecked")
    public synchronized PA allocate( Integer size ) {
      cleanup();
      if( size == null ) {
        size = LibConfig.cfgBufferSize();
      }
      int value  = size.intValue();
      PA  result = getBlock( value );
      if( result == null ) {
        value  = ((value / 1024) + 1) * 1024;
        result = newArray.apply( value );
      }
      return result;
    }
    
    /**
     * Releases the supplied byte array so it can be reallocated later.
     * 
     * @param data   The data that can be reallocated later. Not <code>null</code>.
     */
    public synchronized void release( @NonNull PA data ) {
      cleanup();
      SoftReference<PA> newref = new SoftReference<>( data ); 
      if( allocated.isEmpty() ) {
        allocated.add( newref );
      } else {
        int pos = Collections.binarySearch( allocated, newref, this );
        if( pos < 0 ) {
          pos = -(pos + 1);
        }
        allocated.add( pos, newref );
      }
    }
    
  } /* ENDCLASS */

  private static interface QPredicate<AR> {
    
    boolean test( AR ar1, int idx1, AR ar2, int idx2 );
    
  } /* ENDINTERFACE */
  
} /* ENDCLASS */
