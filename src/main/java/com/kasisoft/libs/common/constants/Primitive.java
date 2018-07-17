package com.kasisoft.libs.common.constants;

import lombok.experimental.*;

import lombok.*;

import com.kasisoft.libs.common.base.*;

import java.util.function.*;

import java.util.*;

import java.lang.ref.*;
import java.lang.reflect.*;

/**
 * Declarations used to identify primitive types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Primitive<PA, O> implements Comparable<Primitive>{

  public static Primitive <boolean [], Boolean  > PBoolean = new Primitive( "PBoolean"  , boolean . class , boolean [] . class , Boolean   . class , Boolean   [] . class , 0                   , 0                   );
  public static Primitive <byte    [], Byte     > PByte    = new Primitive( "PByte"     , byte    . class , byte    [] . class , Byte      . class , Byte      [] . class , Byte    . MIN_VALUE , Byte    . MAX_VALUE );
  public static Primitive <char    [], Character> PChar    = new Primitive( "PChar"     , char    . class , char    [] . class , Character . class , Character [] . class , 0                   , 0                   );
  public static Primitive <short   [], Short    > PShort   = new Primitive( "PShort"    , short   . class , short   [] . class , Short     . class , Short     [] . class , Short   . MIN_VALUE , Short   . MAX_VALUE );
  public static Primitive <int     [], Integer  > PInt     = new Primitive( "PInt"      , int     . class , int     [] . class , Integer   . class , Integer   [] . class , Integer . MIN_VALUE , Integer . MAX_VALUE );
  public static Primitive <long    [], Long     > PLong    = new Primitive( "PLong"     , long    . class , long    [] . class , Long      . class , Long      [] . class , Long    . MIN_VALUE , Long    . MAX_VALUE );
  public static Primitive <float   [], Float    > PFloat   = new Primitive( "PFloat"    , float   . class , float   [] . class , Float     . class , Float     [] . class , 0                   , 0                   );
  public static Primitive <double  [], Double   > PDouble  = new Primitive( "PDouble"   ,double  . class , double  [] . class , Double    . class , Double    [] . class , 0                   , 0                   );
  
  @Getter Class<?>      primitiveClass;
  @Getter Class<PA>     arrayClass;
  
  @Getter Class<O>      objectClass;
  @Getter Class<O[]>    objectArrayClass;
  
  @Getter long          min;
  @Getter long          max;
  
  InternalBuffers<PA>   ibuffers;
  boolean               supportsMinMax;
  String                name;
  
  private Primitive( String pname, Class<?> primitive, Class<PA> arraytype, Class<O> objclazz, Class<O[]> objclazzarray, long minval, long maxval ) {
    name              = pname;
    primitiveClass    = primitive;
    arrayClass        = arraytype;
    objectClass       = objclazz;
    objectArrayClass  = objclazzarray;
    min               = minval;
    max               = maxval;
    supportsMinMax    = minval != maxval;
    ibuffers          = new InternalBuffers<PA>( this::newArray, this::length );
    LocalData.primitivemap.put( primitive        , this );
    LocalData.primitivemap.put( objclazz         , this );
    LocalData.primitivemap.put( arraytype        , this );
    LocalData.primitivemap.put( objectArrayClass , this );
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

} /* ENDENUM */
