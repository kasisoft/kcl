package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.util.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;
import java.util.function.*;

import java.lang.ref.*;

/**
 * Declarations used to identify primitive types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Primitive {

  PBoolean  ( Boolean   . TYPE , Boolean   . class , boolean [] . class , Boolean   [] . class, 0                   , 0                   ) ,
  PByte     ( Byte      . TYPE , Byte      . class , byte    [] . class , Byte      [] . class, Byte    . MIN_VALUE , Byte    . MAX_VALUE ) ,
  PChar     ( Character . TYPE , Character . class , char    [] . class , Character [] . class, 0                   , 0                   ) ,
  PShort    ( Short     . TYPE , Short     . class , short   [] . class , Short     [] . class, Short   . MIN_VALUE , Short   . MAX_VALUE ) ,
  PInt      ( Integer   . TYPE , Integer   . class , int     [] . class , Integer   [] . class, Integer . MIN_VALUE , Integer . MAX_VALUE ) ,
  PLong     ( Long      . TYPE , Long      . class , long    [] . class , Long      [] . class, Long    . MIN_VALUE , Long    . MAX_VALUE ) ,
  PFloat    ( Float     . TYPE , Float     . class , float   [] . class , Float     [] . class, 0                   , 0                   ) ,
  PDouble   ( Double    . TYPE , Double    . class , double  [] . class , Double    [] . class, 0                   , 0                   ) ;
  
  /** Not <code>null</code>. */
  @Getter Class<?>    primitiveClass;
  
  /** Not <code>null</code>. */
  @Getter Class<?>    arrayClass;
  
  /** Not <code>null</code>. */
  @Getter Class<?>    objectClass;
  
  /** Not <code>null</code>. */
  @Getter Class<?>    objectArrayClass;
  
  @Getter long        min;
  @Getter long        max;
  
  @SuppressWarnings("deprecation")
  Buffers             buffers;
  InternalBuffers     ibuffers;
  boolean             supportsMinMax;
  
  /**
   * Sets up this enumeration value.
   * 
   * @param primitive         The primitive type class. Not <code>null</code>.
   * @param objclazz          The object type class. Not <code>null</code>.
   * @param arraytype         The array type class. Not <code>null</code>.
   * @param objectarraytype   The object array type class. Not <code>null</code>. 
   * @param minval            The minimum value.
   * @param maxval            The maximum value.
   */
  Primitive( Class<?> primitive, Class<?> objclazz, Class<?> arraytype, Class<?> objectarraytype, long minval, long maxval ) {
    primitiveClass    = primitive;
    objectClass       = objclazz;
    objectArrayClass  = objectarraytype;
    arrayClass        = arraytype;
    min               = minval;
    max               = maxval;
    buffers           = null;
    supportsMinMax    = minval != maxval;
    LocalData.primitivemap.put( primitive       , this );
    LocalData.primitivemap.put( objclazz        , this );
    LocalData.primitivemap.put( arraytype       , this );
    LocalData.primitivemap.put( objectarraytype , this );
    ibuffers          = new InternalBuffers( this );
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
  public <T> T newArray( int size ) {
    switch( this ) {
    case PBoolean : return (T) new boolean [ size ];
    case PByte    : return (T) new byte    [ size ];
    case PChar    : return (T) new char    [ size ];
    case PShort   : return (T) new short   [ size ];
    case PInt     : return (T) new int     [ size ];
    case PLong    : return (T) new long    [ size ];
    case PFloat   : return (T) new float   [ size ];
    /* case PDouble: */
    default       : return (T) new double  [ size ];
    }
  }

  /**
   * Returns an array of this type consisting of the supplied number of items.
   * 
   * @param size   The number of items.
   * 
   * @return   The array of the corresponding object type. Not <code>null</code>.
   */
  public <T> T newObjectArray( int size ) {
    switch( this ) {
    case PBoolean : return (T) new Boolean    [ size ];
    case PByte    : return (T) new Byte       [ size ];
    case PChar    : return (T) new Character  [ size ];
    case PShort   : return (T) new Short      [ size ];
    case PInt     : return (T) new Integer    [ size ];
    case PLong    : return (T) new Long       [ size ];
    case PFloat   : return (T) new Float      [ size ];
    /* case PDouble: */
    default       : return (T) new Double     [ size ];
    }
  }

  /**
   * Returns the {@link Buffers} instance for this type.
   * 
   * @return   The {@link Buffers} instance for this type. Not <code>null</code>.
   * 
   * @deprecated [09-Aug-2015:KASI]   This function will be removed as the returned type Buffers will be removed. Use
   *                                  it's functionality directly now.
   */
  @SuppressWarnings("deprecation")
  @Deprecated
  public synchronized <T> Buffers<T> getBuffers() {
    if( buffers == null ) {
      buffers  = Buffers.newBuffers( this );
    }
    return buffers;
  }
  
  /**
   * Returns the length of an array instance.
   * 
   * @param arrayobj    An array instance. Not <code>null</code>.
   * 
   * @return   The length of an array instance.
   */
  public int length( @NonNull Object arrayobj ) {
    boolean primitivevariety = arrayobj.getClass() == arrayClass;
    if( primitivevariety ) {
      switch( this ) {
      case PBoolean : return ((boolean []) arrayobj).length;
      case PByte    : return ((byte    []) arrayobj).length;
      case PChar    : return ((char    []) arrayobj).length;
      case PShort   : return ((short   []) arrayobj).length;
      case PInt     : return ((int     []) arrayobj).length;
      case PLong    : return ((long    []) arrayobj).length;
      case PFloat   : return ((float   []) arrayobj).length;
        /* case PDouble: */
      default       : return ((double  []) arrayobj).length;
      }
    } else {
      return ((Object[]) arrayobj).length;
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
  public synchronized <T> T allocate() {
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
  public synchronized <T> T allocate( Integer size ) {
    getBuffers();
    return ((InternalBuffers<T>) ibuffers).allocate( size );
  }  
  
  /**
   * Releases the supplied byte array so it can be reallocated later.
   * 
   * @param data   The data that can be reallocated later. Not <code>null</code>.
   */
  public synchronized <T> void release( @NonNull T data ) {
    getBuffers();
    ((InternalBuffers<T>) ibuffers).release( data );
  }

  /**
   * Executes the supplied function while providing a buffer.
   * 
   * @param function   The function that will be executed using the buffer. Not <code>null</code>.
   * 
   * @return   The result of the supplied function. Maybe <code>null</code>.
   */
  public <T,R> R withBuffer( @NonNull Function<T,R> function ) {
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
  public <T,R> R withBuffer( Integer size, Function<T,R> function ) {
    T buffer = allocate( size );
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
  public <T> void withBufferDo( Consumer<T> consumer ) {
    withBufferDo( null, consumer ); 
  }

  /**
   * Executes the supplied function while providing a buffer.
   * 
   * @param size       The requested size of the returned buffer. A value of <code>null</code> means that the default 
   *                   size has to be used (see {@link CommonProperty#BufferCount}).
   * @param function   The function that will be executed using the buffer. Not <code>null</code>.
   */
  public <T> void withBufferDo( Integer size, Consumer<T> consumer ) {
    T buffer = allocate( size );
    try {
      consumer.accept( buffer );
    } finally {
      release( buffer );
    }
  }

  private static class LocalData {
    
    private static Map<Class<?>,Primitive>   primitivemap = new Hashtable<>();
    
  } /* ENDCLASS */
  
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class InternalBuffers<T> {

    List<SoftReference<T>>        allocated;
    Primitive                     type;
    Comparator<SoftReference<T>>  comparator0;
    
    /**
     * Initialises these instance with the supplied primitive type identification.
     * 
     * @param primitive   The primitive type identification. Not <code>null</code>.
     */
    private InternalBuffers( @NonNull Primitive primitive ) {
      allocated   = new ArrayList<>();
      type        = primitive;
      comparator0 = new Comparator<SoftReference<T>>() {

        @Override
        public int compare( SoftReference<T> o1, SoftReference<T> o2 ) {
          int l1 = length( o1 );
          int l2 = length( o2 );
          if( (l1 == 0) || (l2 == 0) ) {
            // this element have been cleared by the GC so return 0 in order to sustain the current position
            return 0;
          }
          return l1 - l2;
        }
        
      };
    }
    
    private void cleanup() {
      for( int i = allocated.size() - 1; i >= 0; i-- ) {
        if( allocated.get(i).get() == null ) {
          allocated.remove(i);
        }
      }
    }
    
    /**
     * Returns a block of data matching the supplied size constraint.
     * 
     * @param size   The size which is requested.
     * 
     * @return   The block with the apropriate space or <code>null</code>.
     */
    private T getBlock( int size ) {
      if( allocated.isEmpty() ) {
        return null;
      }
      cleanup();
      for( int i = 0; i < allocated.size(); i++ ) {
        SoftReference<T> ref    = allocated.get(i);
        T                result = ref.get();
        if( length( ref ) >= size ) {
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
    public synchronized T allocate( Integer size ) {
      if( size == null ) {
        size = CommonProperty.BufferCount.getValue( System.getProperties() );
      }
      int value  = size.intValue();
      T   result = getBlock( value );
      if( result == null ) {
        value  = ((value / 1024) + 1) * 1024;
        result = (T) type.newArray( value );
      }
      return result;
    }
    
    /**
     * Releases the supplied byte array so it can be reallocated later.
     * 
     * @param data   The data that can be reallocated later. Not <code>null</code>.
     */
    public synchronized void release( @NonNull T data ) {
      cleanup();
      SoftReference<T> newref = new SoftReference<>( data ); 
      if( allocated.isEmpty() ) {
        allocated.add( newref );
      } else {
        int pos = Collections.binarySearch( allocated, newref, comparator0 );
        if( pos < 0 ) {
          pos = -(pos + 1);
        }
        allocated.add( pos, newref );
      }
    }
    
    private int length( SoftReference<T> data ) {
      T content = data.get();
      if( content != null ) {
        return type.length( content );
      } else {
        return 0;
      }
    }
    
  } /* ENDCLASS */
  
} /* ENDENUM */
