package com.kasisoft.libs.common.old.internal.constants;

import com.kasisoft.libs.common.old.base.LibConfig;
import com.kasisoft.libs.common.old.constants.CommonProperty;
import com.kasisoft.libs.common.old.constants.Primitive;
import com.kasisoft.libs.common.types.TriFunction;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import java.lang.ref.SoftReference;
import java.lang.reflect.Array;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrimitiveImpl<PA, O> implements Primitive<PA, O> {
  
  public static final Primitive <boolean [], Boolean  > INTERNAL_BOOLEAN = new PrimitiveImpl<>( "PBoolean"  , boolean . class , boolean [] . class , Boolean   . class , Boolean   [] . class , PrimitiveImpl::toPrimitive, PrimitiveImpl::toObject, PrimitiveImpl::isEqual, PrimitiveImpl::randomBoolean , Arrays::copyOfRange, null                   , 0                   , 0                   );
  public static final Primitive <byte    [], Byte     > INTERNAL_BYTE    = new PrimitiveImpl<>( "PByte"     , byte    . class , byte    [] . class , Byte      . class , Byte      [] . class , PrimitiveImpl::toPrimitive, PrimitiveImpl::toObject, PrimitiveImpl::isEqual, PrimitiveImpl::randomByte    , Arrays::copyOfRange, PrimitiveImpl::isBigger, Byte    . MIN_VALUE , Byte    . MAX_VALUE );
  public static final Primitive <char    [], Character> INTERNAL_CHAR    = new PrimitiveImpl<>( "PChar"     , char    . class , char    [] . class , Character . class , Character [] . class , PrimitiveImpl::toPrimitive, PrimitiveImpl::toObject, PrimitiveImpl::isEqual, PrimitiveImpl::randomChar    , Arrays::copyOfRange, null                   , 0                   , 0                   );
  public static final Primitive <short   [], Short    > INTERNAL_SHORT   = new PrimitiveImpl<>( "PShort"    , short   . class , short   [] . class , Short     . class , Short     [] . class , PrimitiveImpl::toPrimitive, PrimitiveImpl::toObject, PrimitiveImpl::isEqual, PrimitiveImpl::randomShort   , Arrays::copyOfRange, PrimitiveImpl::isBigger, Short   . MIN_VALUE , Short   . MAX_VALUE );
  public static final Primitive <int     [], Integer  > INTERNAL_INT     = new PrimitiveImpl<>( "PInt"      , int     . class , int     [] . class , Integer   . class , Integer   [] . class , PrimitiveImpl::toPrimitive, PrimitiveImpl::toObject, PrimitiveImpl::isEqual, PrimitiveImpl::randomInt     , Arrays::copyOfRange, PrimitiveImpl::isBigger, Integer . MIN_VALUE , Integer . MAX_VALUE );
  public static final Primitive <long    [], Long     > INTERNAL_LONG    = new PrimitiveImpl<>( "PLong"     , long    . class , long    [] . class , Long      . class , Long      [] . class , PrimitiveImpl::toPrimitive, PrimitiveImpl::toObject, PrimitiveImpl::isEqual, PrimitiveImpl::randomLong    , Arrays::copyOfRange, PrimitiveImpl::isBigger, Long    . MIN_VALUE , Long    . MAX_VALUE );
  public static final Primitive <float   [], Float    > INTERNAL_FLOAT   = new PrimitiveImpl<>( "PFloat"    , float   . class , float   [] . class , Float     . class , Float     [] . class , PrimitiveImpl::toPrimitive, PrimitiveImpl::toObject, PrimitiveImpl::isEqual, PrimitiveImpl::randomFloat   , Arrays::copyOfRange, PrimitiveImpl::isBigger, 0                   , 0                   );
  public static final Primitive <double  [], Double   > INTERNAL_DOUBLE  = new PrimitiveImpl<>( "PDouble"   , double  . class , double  [] . class , Double    . class , Double    [] . class , PrimitiveImpl::toPrimitive, PrimitiveImpl::toObject, PrimitiveImpl::isEqual, PrimitiveImpl::randomDouble  , Arrays::copyOfRange, PrimitiveImpl::isBigger, 0                   , 0                   );
  
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
  
  public PrimitiveImpl( 
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

  @Override
  public int indexOf( @NonNull PA buffer, @NonNull PA sequence ) {
    return indexOf( buffer, sequence, 0 );
  }
  
  @Override
  public int indexOf( @NonNull PA buffer, @NonNull PA sequence, int pos ) {
    return indexOfOp( buffer, sequence, pos, true, ($idx, $val) -> $idx < $val, $ -> $ + 1 );
  }
  
  @Override
  public int lastIndexOf( @NonNull PA buffer, @NonNull PA sequence ) {
    return lastIndexOf( buffer, sequence, 0 );
  }

  @Override
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
  
  @Override
  public PA insert( @NonNull PA source, @NonNull PA additional, int index ) {
    int destinationlength = length( source     );
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

  @Override
  public <R> R forValues( O[] values, BiFunction<O, R, R> func ) {
    return forValues( values, null, func );
  }
  
  @Override
  public <R> R forValues( O[] values, R initial, BiFunction<O, R, R> func ) {
    R   result = initial;
    int length = length( values );
    for( int i = 0; i < length; i++ ) {
      result = func.apply( values[i], result );
    }
    return result;
  }

  @Override
  public <R> R forValues( PA values, BiFunction<O, R, R> func ) {
    return forValues( values, null, func );
  }
  
  @Override
  public <R> R forValues( PA values, R initial, BiFunction<O, R, R> func ) {
    R   result = initial;
    int length = length( values );
    for( int i = 0; i < length; i++ ) {
      result = func.apply( (O) Array.get( values, i ), result );
    }
    return result;
  }
  
  @Override
  public O or( PA values ) {
    return booleanOp( values, $ -> $, Boolean.FALSE, Boolean.TRUE);
  }

  @Override
  public O and( PA values ) {
    return booleanOp( values, $ -> !$, Boolean.TRUE, Boolean.FALSE );
  }

  @Override
  public O or( O ... values ) {
    return booleanOp( values, $ -> $, Boolean.FALSE, Boolean.TRUE);
  }

  @Override
  public O and( O ... values ) {
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

  @Override
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

  @Override
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

  @Override
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
       
  @Override
  public PA copy( @NonNull PA input ) {
    int length = length( input );
    return copy( input, length );
  }

  @Override
  public PA copy( @NonNull PA input, int newlength ) {
    return copy.apply( input, 0, newlength );
  }

  @Override
  public PA copyOfRange( @NonNull PA input, int from, int to ) {
    return copy.apply( input, from, to );
  }

  @Override
  public O randomValue() {
    return (O) randomValue.get();
  }

  @Override
  public PA randomArray( int length ) {
    return randomArray( length, null );
  }
  
  @Override
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

  @Override
  public boolean compare( @NonNull PA data, @NonNull PA tocompare ) {
    int l1 = length( data );
    if( l1 == 0 ) {
      return false;
    }
    return compare( data, tocompare, 0 );
  }
  
  @Override
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
  
  @Override
  public PA cleanup( PA input ) {
    PA result = null;
    return result;
  }
  
  @Override
  public O[] cleanup( O ... input ) {
    return cleanup( null, input );
  }
  
  @Override
  public O[] cleanup( Predicate<O> isNotSet, O ... input ) {
    O[] result = null;
    if( input != null ) {
      if( isNotSet == null ) {
        isNotSet = $ -> $ == null;
      }
      Predicate<O> isSet = isNotSet.negate();
      int count = countUnset( isNotSet, input );
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

  @Override
  public int countSet( O ... input ) {
    return countSet( null, input );
  }
  
  @Override
  public int countSet( Predicate<O> isSet, O ... input ) {
    if( isSet == null ) {
      isSet = $ -> $ != null;
    }
    return count( input, isSet );
  }
  
  @Override
  public int countUnset( O ... input ) {
    return countUnset( null, input );
  }
  
  @Override
  public int countUnset( Predicate<O> isNotSet, O ... input ) {
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

  @Override
  public List<O> toList( PA primitiveArray ) {
    return new ArrayList<>( Arrays.asList( toObjectArray( primitiveArray ) ) );
  }

  @Override
  public O[] toObjectArray( PA primitiveArray ) {
    O[] result = null;
    if( primitiveArray != null ) {
      int length  = length( primitiveArray );
      result      = newObjectArray( length );
      toObject.accept( primitiveArray, result );
    }
    return result;
  }

  @Override
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

  @Override
  public boolean supportsMinMax() {
    return supportsMinMax;
  }
  
  @Override
  public long getUnsignedMax() {
    if( this == PLong ) {
      return 0;
    }
    return 2 * max + 1;
  }
  
  @Override
  public PA newArray( int size ) {
    return (PA) Array.newInstance( primitiveClass, size );
  }

  @Override
  public O[] newObjectArray( int size ) {
    return (O[]) Array.newInstance( objectClass, size );
  }

  @Override
  public int length( Object arrayobj ) {
    if( arrayobj != null ) {
      return Array.getLength( arrayobj );
    } else {
      return 0;
    }
  }
  
  @Override
  public synchronized PA allocate() {
    return allocate( null );
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public synchronized PA allocate( Integer size ) {
    return ibuffers.allocate( size );
  }  
  
  @Override
  public synchronized void release( @NonNull PA data ) {
    ibuffers.release( data );
  }

  @Override
  public <R> R withBuffer( @NonNull Function<PA, R> function ) {
    return withBuffer( null, function );
  }

  @Override
  public <R> R withBuffer( Integer size, Function<PA, R> function ) {
    PA buffer = allocate( size );
    try {
      return function.apply( buffer );
    } finally {
      release( buffer );
    }
  }

  @Override
  public void withBufferDo( Consumer<PA> consumer ) {
    withBufferDo( null, consumer ); 
  }

  @Override
  public void withBufferDo( Integer size, Consumer<PA> consumer ) {
    PA buffer = allocate( size );
    try {
      consumer.accept( buffer );
    } finally {
      release( buffer );
    }
  }
  
  @Override
  public int compareTo( Primitive o ) {
    return name.compareTo( ((PrimitiveImpl) o).name );
  }

  @Override
  public String toString() {
    return name;
  }

  public static Primitive[] valuesImpl() {
    Primitive[] result = LocalData.primitivemap.values().toArray( new Primitive[ LocalData.primitivemap.size() ] );
    Arrays.sort( result );
    return result;
  }
  
  public static Primitive byTypeImpl( Object obj ) {
    if( obj == null ) {
      return null;
    } else {
      return LocalData.primitivemap.get( obj.getClass() );
    }
  }

  public static boolean isEqual( boolean[] dest1, int idx1, boolean[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  public static boolean isEqual( byte[] dest1, int idx1, byte[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  public static boolean isEqual( char[] dest1, int idx1, char[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  public static boolean isEqual( short[] dest1, int idx1, short[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  public static boolean isEqual( int[] dest1, int idx1, int[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  public static boolean isEqual( long[] dest1, int idx1, long[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  public static boolean isEqual( float[] dest1, int idx1, float[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }

  public static boolean isEqual( double[] dest1, int idx1, double[] dest2, int idx2 ) {
    return dest1[idx1] == dest2[idx2];
  }
  
  public static void toPrimitive( Boolean[] array, boolean[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toPrimitive( Byte[] array, byte[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toPrimitive( Character[] array, char[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toPrimitive( Short[] array, short[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toPrimitive( Integer[] array, int[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toPrimitive( Long[] array, long[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toPrimitive( Float[] array, float[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toPrimitive( Double[] array, double[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toObject( boolean[] array, Boolean[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toObject( byte[] array, Byte[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toObject( char[] array, Character[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toObject( short[] array, Short[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toObject( int[] array, Integer[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toObject( long[] array, Long[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toObject( float[] array, Float[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }

  public static void toObject( double[] array, Double[] dest ) {
    for( int i = 0; i < array.length; i++ ) {
      dest[i] = array[i];
    }
  }
  
  public static double randomVal() {
    return Math.random() * System.currentTimeMillis();
  }

  public static boolean randomBoolean() {
    long val = (long) randomVal();
    return (boolean) ((val % 2) == 0);
  }

  public static double randomDouble() {
    return (randomBoolean() ? 1.0 : -1.0) * randomVal();
  }
  
  public static float randomFloat() {
    return (float) randomDouble();
  }

  public static long randomLong() {
    return (long) randomDouble();
  }

  public static int randomInt() {
    return (int) randomLong();
  }

  public static short randomShort() {
    return (short) randomLong();
  }

  public static byte randomByte() {
    return (byte) randomLong();
  }

  public static char randomChar() {
    return (char) randomShort();
  }
  
  public static boolean isBigger( Byte c1, Byte c2 ) {
    return c1.byteValue() > c2.byteValue();
  }

  public static boolean isBigger( Short c1, Short c2 ) {
    return c1.shortValue() > c2.shortValue();
  }

  public static boolean isBigger( Integer c1, Integer c2 ) {
    return c1.intValue() > c2.intValue();
  }
  
  public static boolean isBigger( Long c1, Long c2 ) {
    return c1.longValue() > c2.longValue();
  }
  
  public static boolean isBigger( Float c1, Float c2 ) {
    return c1.floatValue() > c2.floatValue();
  }

  public static boolean isBigger( Double c1, Double c2 ) {
    return c1.doubleValue() > c2.doubleValue();
  }
  
  private static interface QPredicate<AR> {
    
    boolean test( AR ar1, int idx1, AR ar2, int idx2 );
    
  } /* ENDINTERFACE */
  
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
  
} /* ENDCLASS */
