package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Simple class used to provide buffers for temporary use. This is currently a straight forward implementation and not 
 * optimized since I don't expect heavy usage. Next optimisation step would be the use of interval partitioning. 
 * Nevertheless this type provides buffers meant to be reused especially in multi-threading environments.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [09-Aug-2015:KASI]    This class will be removed with version 2.0. Use the corresponding methods of the
 *                                   type {@link Primitive} .
 */
@Deprecated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Buffers<T> {

  Primitive   type;
  
  /**
   * Initialises these instance with the supplied primitive type identification.
   * 
   * @param primitive   The primitive type identification. Not <code>null</code>.
   */
  private Buffers( @NonNull Primitive primitive ) {
    type        = primitive;
  }
  
  /**
   * Allocates a block with the current default size.
   * 
   * @return  A block with the current default size. Neither <code>null</code> nor empty.
   */
  public synchronized T allocate() {
    return type.allocate();
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
    return type.allocate( size );
  }
  
  /**
   * Releases the supplied byte array so it can be reallocated later.
   * 
   * @param data   The data that can be reallocated later. Not <code>null</code>.
   */
  public synchronized void release( @NonNull T data ) {
    type.release( data );
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
