package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

import java.lang.ref.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Simple class used to provide buffers for temporary use. This is currently a straight forward implementation and not 
 * optimized since I don't expect heavy usage. Next optimisation step would be the use of interval partitioning. 
 * Nevertheless this type provides buffers meant to be reused especially in multi-threading environments.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Buffers<T> {

  List<SoftReference<T>>        allocated;
  Primitive                     type;
  Comparator<SoftReference<T>>  comparator0;
  
  /**
   * Initialises these instance with the supplied primitive type identification.
   * 
   * @param primitive   The primitive type identification. Not <code>null</code>.
   */
  private Buffers( @NonNull Primitive primitive ) {
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
   * Allocates a block with the current default size.
   * 
   * @return  A block with the current default size. Neither <code>null</code> nor empty.
   */
  public synchronized T allocate() {
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
  
  /**
   * Creates a Buffers management depending on the supplied type.
   * 
   * @param primitive   An identification for the primitive type. Not <code>null</code>.
   * 
   * @return   The instance used to manage buffers of the desired type. The result can be
   *           safely casted to the desired Buffers instance. Not <code>null</code>.
   */
  @SuppressWarnings("unchecked")
  public static Buffers newBuffers( @NonNull Primitive primitive ) {
    switch( primitive ) {
    case PBoolean : return new Buffers<boolean[]>( primitive );
    case PByte    : return new Buffers<byte   []>( primitive );
    case PChar    : return new Buffers<char   []>( primitive );
    case PDouble  : return new Buffers<double []>( primitive );
    case PFloat   : return new Buffers<float  []>( primitive );
    case PInt     : return new Buffers<int    []>( primitive );
    case PLong    : return new Buffers<long   []>( primitive );
    /* Primitive.Short */ 
    default       : return new Buffers<short  []>( primitive );
    }
  }
  
} /* ENDCLASS */
