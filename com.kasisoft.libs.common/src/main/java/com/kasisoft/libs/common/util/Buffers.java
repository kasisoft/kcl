/**
 * Name........: Buffers
 * Description.: Simple class used to provide buffers for temporary use. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

import java.lang.ref.*;

import lombok.*;

/**
 * Simple class used to provide buffers for temporary use. This is currently a straight forward implementation and not 
 * optimized since I don't expect heavy usage. Next optimisation step would be the use of interval partitioning. 
 * Nevertheless this type provides buffers meant to be reused especially in multi-threading environments.
 */
public class Buffers<T> {

  private List<SoftReference<T>>   allocated;
  private Primitive                type;
  
  /**
   * Initialises these instance with the supplied primitive type identification.
   * 
   * @param primitive   The primitive type identification. Not <code>null</code>.
   */
  private Buffers( @NonNull Primitive primitive ) {
    allocated = new ArrayList<SoftReference<T>>();
    type      = primitive;
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
    /** @ks.todo [11-Jan-2009:KASI] the access could be improved when the blocks are sorted depending on their length. */ 
    for( int i = 0; i < allocated.size(); i++ ) {
      SoftReference<T> ref = allocated.get(i);
      if( type.length( ref.get() ) >= size ) {
        T result = ref.get();
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
    SoftReference<T> ref = new SoftReference<T>( data ); 
    if( allocated.isEmpty() ) {
      allocated.add( ref );
    } else {
      int last    = allocated.size() - 1;
      int length  = type.length( data );
      if( type.length( allocated.get( last ).get() ) < length ) {
        allocated.add( ref );
      } else {
        // the insertion makes sure that the list is always sorted by the size of the blocks
        for( int i = 0; i < allocated.size(); i++ ) {
          if( type.length( allocated.get(i).get() ) > length ) {
            allocated.add( i, ref );
            break;
          }
        }
      }
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
